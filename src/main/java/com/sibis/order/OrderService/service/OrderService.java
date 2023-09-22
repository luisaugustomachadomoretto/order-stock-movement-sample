package com.sibis.order.OrderService.service;

import com.sibis.order.OrderService.entity.EnumOrderStatus;
import com.sibis.order.OrderService.entity.Order;
import com.sibis.order.OrderService.entity.StockMovement;
import com.sibis.order.OrderService.repository.OrderRepository;
import com.sibis.order.OrderService.repository.StockMovementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    public static final String CANNOT_CREATE_AN_ORDER_CAUSE_QUANTITY_CANNOT_BE_EQUALS_TO_0 = "Cannot create an order cause quantity cannot be equals to 0";
    public static final String CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_USER = "Cannot create an order with an empty user!";
    public static final String CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_ITEM = "Cannot create an order with an empty item!";
    public static final String MAIL_COM = "shop@mail.com";
    public static final String ITEM_NOT_AVAILABLE = "Item not available";
    public static final String CANNOT_MOVE_A_ITEM_FROM_STOCK_TO_A_COMPLETED_ORDER = "Cannot move a item from stock to a completed order!";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StockMovementRepository stockMovementRepository;
    @Autowired
    private EmailService emailService;

    public List<Order> findAllOrders() {
        return this.orderRepository.findAll();
    }

    @Transactional
    public Order createOrUpdate(Order order) throws Exception {
        //Validate order
        this.validateOrder(order);
        //Check stock before doing a movent from stock to order
        this.doAStockMovement(order);
        //Order is completed so we send and email to advice
        if (order.getStatus().equals(EnumOrderStatus.COMPLETED)) {
            this.sendEmail(order);
        }
        return this.orderRepository.save(order);
    }

    private void validateOrder(Order order) throws Exception {
        if (order.getQuantity().equals(0)) {
            log.error(CANNOT_CREATE_AN_ORDER_CAUSE_QUANTITY_CANNOT_BE_EQUALS_TO_0);
            throw new Exception(CANNOT_CREATE_AN_ORDER_CAUSE_QUANTITY_CANNOT_BE_EQUALS_TO_0);
        }
        if (order.getUser() == null) {
            log.error(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_USER);
            throw new Exception(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_USER);
        }
        if (order.getItem() == null) {
            log.error(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_ITEM);
            throw new Exception(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_ITEM);
        }
    }

    private void doAStockMovement(Order order) throws Exception {
        final Optional<StockMovement> stockMovementOptional = this
                .stockMovementRepository.findStockMovementByItem(order.getItem());

        //get status from order before update if it already exists
        Optional<Order> order1 = this.orderRepository.findById(order.getId());
        if (
                order1.isPresent() &&
                        order1
                                .get()
                                .getStatus()
                                .equals(EnumOrderStatus.COMPLETED)
        ) {
            log.error(CANNOT_MOVE_A_ITEM_FROM_STOCK_TO_A_COMPLETED_ORDER);
            throw new Exception(CANNOT_MOVE_A_ITEM_FROM_STOCK_TO_A_COMPLETED_ORDER);
        }

        //Item not available yet
        if (!stockMovementOptional.isPresent()) {
            log.error(ITEM_NOT_AVAILABLE);
            throw new Exception(ITEM_NOT_AVAILABLE);
        }

        //Calculate movement from stock to order
        StockMovement stockMovement = stockMovementOptional.get();
        final Integer quantityAvaliable = stockMovement.getQuantity();
        final int newQuantityAvailable = quantityAvaliable - order.getQuantity();

        if (quantityAvaliable.equals(0) ||
                newQuantityAvailable < 0
        ) {
            log.error(ITEM_NOT_AVAILABLE);
            throw new Exception(ITEM_NOT_AVAILABLE);
        }

        stockMovement.setQuantity(newQuantityAvailable);
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
                        "Save this email to track your order!"

        );
    }

    @Transactional
    public void delete(Long orderID) {
        this.orderRepository.deleteById(orderID);
    }

    public List<Order> findAllOrdersWithStockMovements() {
        return null;
    }
}
