package com.sibis.order.OrderService.controller;

import com.sibis.order.OrderService.entity.*;
import com.sibis.order.OrderService.service.ItemService;
import com.sibis.order.OrderService.service.OrderService;
import com.sibis.order.OrderService.service.StockMovementService;
import com.sibis.order.OrderService.service.UserService;
import com.sibis.order.OrderService.vo.OrderVO;
import lombok.NonNull;
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

    @GetMapping("orders/stock-movement-report")
    public ResponseEntity<List<OrderStockMovement>> getAllOrdersWithStockMovement() {
        return ResponseEntity.ok(this.stockMovementService.findAllOrdersWithStockMovements());
    }

    @GetMapping("orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(this.orderService.findAllOrders());
    }

    @SneakyThrows
    @PostMapping("orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderVO orderVO) {
        final Order newOrder = this.orderService.create(orderVO);

        log.info(String.format("New Order values are: %s", orderVO));

        return ResponseEntity.ok(newOrder);
    }

    @SneakyThrows
    @PutMapping("orders/{orderID}/{quantity}/{status}/update")
    public ResponseEntity<Order> updateOrder(
            @NonNull @PathVariable Long orderID,
            @NonNull @PathVariable Integer quantity,
            @NonNull @PathVariable EnumOrderStatus status)  {

        final Order updateOrder = this.orderService
                .update(orderID,quantity,status);

        log.info(
                String.format(
                        "Update Order values %s",
                        updateOrder)
        );

        return ResponseEntity.ok(updateOrder);
    }

    @DeleteMapping("orders/{orderID}/delete")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Long orderID) {
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

    @DeleteMapping("itens/{itemID}/delete")
    public ResponseEntity<Boolean> deleteItem(@PathVariable Long itemID) {
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

    @DeleteMapping("users/{userID}/delete")
    public ResponseEntity<Boolean> delete(@PathVariable Long userID) {
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

    @DeleteMapping("stock-movements/{stockMovementID}/delete")
    public ResponseEntity<Boolean> deleteStockMovement(@PathVariable Long stockMovementID) {
        this.stockMovementService.delete(stockMovementID);
        return ResponseEntity.ok(true);
    }
}
