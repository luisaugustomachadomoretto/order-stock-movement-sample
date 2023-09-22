## Simple Order Manager API

This API allows users to create, read, update, and delete orders and items. 
It also automatically fulfills orders as soon as the item stock allows it.

### Technologies used:

- Java 8
- Spring Boot
- Spring JPA
- PostgreSQL
- GIT
- log4j
- Docker

### Requirements:

The system should be able to provide the following features:
- create, read, update and delete and list all entities;
- when an order is created, it should try to satisfy it with the current stock.;
- when a stock movement is created, the system should try to attribute it to an order that isn't complete;
- when an order is complete, send a notification by email to the user that created it;
- trace the list of stock movements that were used to complete the order, and vice-versa;
- show current completion of each order;
- Write a log file with: orders completed, stock movements, email sent and errors.

### Entities:

- Item
- StockMovement
- Order
- User

### API routes:

For each one of the following routes we have:
 - GET
 - POST
 - PUT
 - DELETE

So endpoints are declared as bellow:

- api/v1/items: CRUD operations for items
- api/v1/stock-movements: CRUD operations for stock movements
- api/v1/orders: CRUD operations for orders
- api/v1/users: CRUD operations for users

### How to run the project:

- Clone the project repository.
- Change the email configurations from application.yml line 26
```
mail:
  host: smtp.example.com
  port: 587
  username: username
  password: password
```
- build images: 

```bash
sudo docker-compose up -d
```

### How to access Microservice endpoints
