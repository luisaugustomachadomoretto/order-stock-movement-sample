package com.sibis.order.OrderService.controller;

import com.sibis.order.OrderService.entity.Item;
import com.sibis.order.OrderService.entity.Order;
import com.sibis.order.OrderService.entity.StockMovement;
import com.sibis.order.OrderService.entity.User;
import com.sibis.order.OrderService.service.ItemService;
import com.sibis.order.OrderService.service.OrderService;
import com.sibis.order.OrderService.service.StockMovementService;
import com.sibis.order.OrderService.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final ItemService itensService;
    private final UserService userService;
    private final StockMovementService stockMovementService;

    @Autowired
    public OrderController(
            OrderService orderService,
            ItemService itensService,
            UserService userService,
            StockMovementService stockMovementService
    ){
        this.itensService = itensService;
        this.orderService = orderService;
        this.userService = userService;
        this.stockMovementService = stockMovementService;
    }

    @GetMapping("orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(this.orderService.findAllOrders());
    }

    @SneakyThrows
    @PostMapping("orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        final Order newOrder = this.orderService.createOrUpdate(order);

        log.info(String.format("New Order values are: %s", order));

        return ResponseEntity.ok(newOrder);
    }

    @PutMapping("orders")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) throws Exception {

        final Order updateOrder = this.orderService.createOrUpdate(order);

        log.info(String.format("Update Order values %s", order));

        return ResponseEntity.ok(updateOrder);
    }

    @DeleteMapping("orders")
    public ResponseEntity<Boolean> deleteOrder(@RequestBody Long orderID) {
        this.orderService.delete(orderID);
        return ResponseEntity.ok(true);
    }

    @GetMapping("itens")
    public ResponseEntity<List<Item>> getAllItens() {
        return ResponseEntity.ok(
                this.itensService.findAllItens()
        );
    }

    @SneakyThrows
    @PostMapping("itens")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        final Item newItem = this.itensService.createOrUpdate(item);

        log.info(
                String.format(
                        "New Item values are: %s",
                        newItem
                )
        );

        return ResponseEntity.ok(newItem);
    }

    @PutMapping("itens")
    public ResponseEntity<Item> updateItem(@RequestBody Item item) {

        final Item updatedItem = this.itensService.createOrUpdate(item);

        log.info(String.format("Updated Item new values %s", item));

        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("itens")
    public ResponseEntity<Boolean> deleteItem(@RequestBody Long itemID) {
        this.itensService.delete(itemID);
        return ResponseEntity.ok(true);
    }

    @GetMapping("users")
    public ResponseEntity<List<User>> getAllIUsers() {
        return ResponseEntity.ok(
                this.userService.findAllUsers()
        );
    }

    @SneakyThrows
    @PostMapping("users")
    public ResponseEntity<User> create(@RequestBody User user) {
        final User newUser = this.userService.createOrUpdate(user);

        log.info(
                String.format(
                        "New Item values are: %s",
                        user
                )
        );

        return ResponseEntity.ok(newUser);
    }

    @PutMapping("users")
    public ResponseEntity<User> update(@RequestBody User user) {

        final User updatedUser = this.userService.createOrUpdate(user);

        log.info(String.format("Updated User new values %s", user));

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("users")
    public ResponseEntity<Boolean> delete(@RequestBody Long userID) {
        this.userService.delete(userID);
        return ResponseEntity.ok(true);
    }

    @GetMapping("stock-movements")
    public ResponseEntity<List<StockMovement>> getAllStockMovements() {
        return ResponseEntity.ok(
                this.stockMovementService.findAllUMovements()
        );
    }

    @SneakyThrows
    @PostMapping("stock-movements")
    public ResponseEntity<StockMovement> createStockMovement(@RequestBody StockMovement stockMovement) {
        final StockMovement newStock = this.stockMovementService.createOrUpdate(stockMovement);

        log.info(
                String.format(
                        "New Item values are: %s",
                        newStock
                )
        );

        return ResponseEntity.ok(newStock);
    }

    @PutMapping("stock-movements")
    public ResponseEntity<StockMovement> updateStockMovement(@RequestBody StockMovement stockMovement) {

        final StockMovement updatedStock = this.stockMovementService.createOrUpdate(stockMovement);

        log.info(String.format("Updated StockMovement new values %s", stockMovement));

        return ResponseEntity.ok(updatedStock);
    }

    @DeleteMapping("stock-movements")
    public ResponseEntity<Boolean> deleteStockMovement(@RequestBody Long stockMovementID) {
        this.stockMovementService.delete(stockMovementID);
        return ResponseEntity.ok(true);
    }
}
