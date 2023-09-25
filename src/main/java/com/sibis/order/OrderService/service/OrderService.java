package com.sibis.order.OrderService.service;

import com.sibis.order.OrderService.entity.EnumOrderStatus;
import com.sibis.order.OrderService.entity.Order;
import com.sibis.order.OrderService.exception.NotFoundException;
import com.sibis.order.OrderService.repository.ItemRepository;
import com.sibis.order.OrderService.repository.OrderRepository;
import com.sibis.order.OrderService.repository.StockMovementRepository;
import com.sibis.order.OrderService.repository.UserRepository;
import com.sibis.order.OrderService.vo.OrderVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    public static final String CANNOT_CREATE_AN_ORDER_CAUSE_QUANTITY_CANNOT_BE_EQUALS_TO_0 = "Cannot create an order cause quantity cannot be equals to 0";
    public static final String CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_USER = "Cannot create an order with an empty user!";
    public static final String CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_ITEM = "Cannot create an order with an empty item!";
    public static final String MAIL_COM = "shop@mail.com";
    public static final String ITEM_NOT_AVAILABLE = "Item not available";
    public static final String CANNOT_MOVE_A_ITEM_FROM_STOCK_TO_A_COMPLETED_ORDER = "Cannot move a item from stock to a completed order!";
    public static final String ORDER_ID_DOES_NOT_EXIST = "Order ID does not exist!";
    public static final String CANNOT_INCREASE_QUANTITY_ITEM_OUT_OF_STOCK = "Cannot increase quantity. Item out of stock!";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockMovementRepository stockMovementRepository;
    @Autowired
    private EmailService emailService;

    public List<Order> findAllOrders() {
        return this.orderRepository.findAll();
    }

    @Transactional
    public Order create(OrderVO orderVO) throws Exception {

        //Validate order
        var order = this.validateOrder(orderVO);

        //Check stock before doing a movent from stock to order
        this.doAStockMovement(order);

        return this.orderRepository.save(order);
    }

    private Order validateOrder(OrderVO orderVO) throws Exception {

        var order = orderVO.getOrderID() == null ?
                new Order() :
                this.orderRepository.findById(orderVO.getOrderID()).get();

        if (orderVO.getQuantity().equals(0)) {
            log.error(CANNOT_CREATE_AN_ORDER_CAUSE_QUANTITY_CANNOT_BE_EQUALS_TO_0);
            throw new Exception(CANNOT_CREATE_AN_ORDER_CAUSE_QUANTITY_CANNOT_BE_EQUALS_TO_0);
        }
        order.setQuantity(orderVO.getQuantity());

        // Get user from database
        var user = this.userRepository.findById(orderVO.getUserID());
        if (!user.isPresent()) {
            log.error(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_USER);
            throw new Exception(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_USER);
        }
        order.setUser(user.get());

        //Check if item exists
        var item = this.itemRepository.findById(orderVO.getItemID());
        if (!item.isPresent()) {
            log.error(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_ITEM);
            throw new NotFoundException(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_ITEM);
        }
        order.setItem(item.get());
        return order;
    }

    @SneakyThrows
    private void doAStockMovement(Order order) {
        final var stockMovementOptional = this
                .stockMovementRepository.findStockMovementByItem(order.getItem());

        //Item Stock not available yet
        if (!stockMovementOptional.isPresent()) {
            log.error(ITEM_NOT_AVAILABLE);
            throw new NotFoundException(ITEM_NOT_AVAILABLE);
        }

        //Calculate quantity available for the order
        var stockMovement = stockMovementOptional.get();
        final var newQuantityAvailable = stockMovement.getQuantity() - order.getQuantity();

        if (newQuantityAvailable < 0) {
            log.error(ITEM_NOT_AVAILABLE);
            throw new NotFoundException(ITEM_NOT_AVAILABLE);
        }

        stockMovement.decreaseQuantity(order.getQuantity());
        this.stockMovementRepository.save(stockMovement);
        log.warn(
                String.format(
                        "StockMovement quantity has changed: %s",
                        stockMovement
                )
        );
    }


    private void sendEmail(Order order) {
        this.emailService.sendEmail(
                MAIL_COM,
                order.getUser().getEmail(),
                "Your order id:" + order.getId() + " is completed!",
                "Your order is been delivered to you!\n" +
                        "Thanks for choosing us.\n" +
                        "Save this code[" +
                        java.util.UUID.randomUUID().toString() +
                        "] to track your order!"

        );
    }

    @Transactional
    public void delete(Long orderID) {
        this.orderRepository.deleteById(orderID);
    }


    @SneakyThrows
    @Transactional
    public Order update(Long orderID, Integer newQuantity, EnumOrderStatus status) {
        var orderOptional = this.orderRepository.findById(orderID);
        if (!orderOptional.isPresent()) {
            log.error(ORDER_ID_DOES_NOT_EXIST);
            throw new NotFoundException(ORDER_ID_DOES_NOT_EXIST);
        }
        var orderUpdated = orderOptional.get();
        if (orderUpdated
                .getStatus()
                .equals(EnumOrderStatus.COMPLETED)
        ) {
            log.error(CANNOT_MOVE_A_ITEM_FROM_STOCK_TO_A_COMPLETED_ORDER);
            throw new Exception(CANNOT_MOVE_A_ITEM_FROM_STOCK_TO_A_COMPLETED_ORDER);
        }

        final var diffQuantity = newQuantity - orderUpdated.getQuantity();

        var stockMovement = this.stockMovementRepository
                .findStockMovementByItem(orderUpdated.getItem())
                .get();

        //Adding quantity to the order. We need to remove from stock movement
        if (diffQuantity > 0) {
            if (stockMovement.getQuantity() < diffQuantity) {
                throw new Exception(CANNOT_INCREASE_QUANTITY_ITEM_OUT_OF_STOCK);
            }
            stockMovement.decreaseQuantity(diffQuantity);
        } else {//Decrease quantity from order. We need to give it back to the stock movement
            stockMovement.increaseQuantity(diffQuantity * -1);
        }

        //Update movement
        this.stockMovementRepository.save(stockMovement);
        //Set new quantity
        orderUpdated.setQuantity(newQuantity);

        //Log the order when the status is completed
        if (status.equals(EnumOrderStatus.COMPLETED)) {
            log.info("Order completed %s", orderUpdated);
            //Order is completed so we send and email to advice
            this.sendEmail(orderUpdated);
        }
        orderUpdated.setStatus(status);
        //Save and return
        return this.orderRepository.save(orderUpdated);
    }
}
