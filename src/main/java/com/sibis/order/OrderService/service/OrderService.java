package com.sibis.order.OrderService.service;

import com.sibis.order.OrderService.entity.EnumOrderStatus;
import com.sibis.order.OrderService.entity.Order;
import com.sibis.order.OrderService.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    public List<Order> findAllOrders() {
        return this.orderRepository.findAll();
    }

    @Transactional
    public Order createOrUpdate(Order order) throws Exception {

        if (order.getQuantity().equals(0)) {
            log.error(CANNOT_CREATE_AN_ORDER_CAUSE_QUANTITY_CANNOT_BE_EQUALS_TO_0);
            throw new Exception(CANNOT_CREATE_AN_ORDER_CAUSE_QUANTITY_CANNOT_BE_EQUALS_TO_0);
        }
        if(order.getUser()==null){
            log.error(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_USER);
            throw new Exception(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_USER);
        }
        if(order.getItem()==null){
            log.error(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_ITEM);
            throw new Exception(CANNOT_CREATE_AN_ORDER_WITH_AN_EMPTY_ITEM);
        }

        //Order is completed so we send and email to advice
        if (order.getStatus() != null && order.getStatus().equals(EnumOrderStatus.COMPLETED)) {
            this.emailService.sendEmail(
                    MAIL_COM,
                    order.getUser().getEmail(),
                    "Your order id:" + order.getId() + " is completed!",
                    "Your order is been delivered to you!\n" +
                            "Thanks for choosing us.\n" +
                            "Save this email to track your order!"

            );
        }

        return this.orderRepository.save(order);
    }

    @Transactional
    public void delete(Long orderID) {
        this.orderRepository.deleteById(orderID);
    }
}
