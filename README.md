# Multi-Vendor E-Commerce Marketplace

A reference UI lives in `frontend/index.html` — open it directly in a browser for a
Shop / Sell / Admin demo (see `frontend/README.md` for details on wiring it to the API below).

## Backend

A Spring Boot REST API for a multi-vendor marketplace, generated in the same
**Router → Controller → Service → Repository → Entity** pattern as the sample project.

## Package layout

```
com.example.marketplace
├── entity/       JPA entities (@Entity, plain getters/setters, no Lombok)
├── repository/   JpaRepository<Entity, Integer> interfaces
├── service/      @Service beans with CRUD + orElseThrow business logic
├── controller/   @Component classes with ServerRequest/ServerResponse handlers
└── router/       @Configuration classes wiring RouterFunction<ServerResponse> beans
```

Every request flows the same way as the sample `User`/`Product`/`Order` module:

```
Router (maps URL -> controller method reference)
   -> Controller (parses ServerRequest, calls Service, wraps result in
      Map.of("status", "message", "data") via ServerResponse)
      -> Service (business logic, orElseThrow(new RuntimeException(...)))
         -> Repository (JpaRepository)
            -> Entity (JPA @Table)
```

## Modules generated

| Entity      | Table          | Base route            |
|-------------|----------------|------------------------|
| User        | users          | /api/users             |
| Seller      | sellers        | /api/sellers            |
| Category    | categories     | /api/categories         |
| Product     | products       | /api/products           |
| Inventory   | inventory      | /api/inventory          |
| Address     | addresses      | /api/addresses          |
| Cart        | carts          | /api/carts              |
| CartItem    | cart_items     | /api/cart-items         |
| Wishlist    | wishlist_items | /api/wishlist           |
| Coupon      | coupons        | /api/coupons            |
| Order       | orders         | /api/orders             |
| OrderItem   | order_items    | /api/order-items        |
| Payment     | payments       | /api/payments           |
| Review      | reviews        | /api/reviews            |

Each module exposes the same five endpoints (matching the sample project's convention):

```
POST /api/<route>/create   body: entity JSON               -> create
GET  /api/<route>/index                                    -> list all
POST /api/<route>/show     body: { "id": 1 }                -> get by id
POST /api/<route>/update   body: { "id": 1, ...fields }     -> update
POST /api/<route>/delete   body: { "id": 1 }                -> delete
```

All responses are JSON in the shape `{ "status": true/false, "message": "...", "data": ... }`.

## Example flow

```
POST /api/users/create        { "username": "alice", "email": "alice@mail.com", ... }
POST /api/sellers/create       { "userId": 1, "shopName": "Alice Store", ... }
POST /api/categories/create    { "categoryName": "Electronics", ... }
POST /api/products/create      { "sellerId": 1, "categoryId": 1, "productName": "Headphones", "price": 49.99, ... }
POST /api/inventory/create     { "productId": 1, "stockQuantity": 50, ... }
POST /api/carts/create         { "userId": 1, "active": true }
POST /api/cart-items/create    { "cartId": 1, "productId": 1, "quantity": 2, "price": 49.99 }
POST /api/addresses/create     { "userId": 1, "fullName": "Alice", ... }
POST /api/orders/create        { "userId": 1, "addressId": 1, "totalAmount": 99.98, "orderStatus": "PLACED", ... }
POST /api/order-items/create   { "orderId": 1, "productId": 1, "sellerId": 1, "quantity": 2, "price": 49.99 }
POST /api/payments/create      { "orderId": 1, "paymentMethod": "CARD", "amount": 99.98, "paymentStatus": "PAID" }
POST /api/reviews/create       { "userId": 1, "productId": 1, "rating": 5, "comment": "Great!" }
POST /api/coupons/create       { "code": "SAVE20", "discountPercent": 20, ... }
POST /api/wishlist/create      { "userId": 1, "productId": 1 }
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/marketplace_db
spring.datasource.username=root
spring.datasource.password=your_password
```

Create the database first:

```sql
CREATE DATABASE marketplace_db;
```

## Running

```
mvn spring-boot:run
```

API base URL: `http://localhost:8080`

## What's intentionally left out (extend as needed)

This generated skeleton mirrors your sample project's simplicity: no Spring Security/JWT,
no DTOs/validation annotations, no service-to-service relationships (JPA `@ManyToOne` etc.)
— foreign keys are plain `int` fields, exactly like `Order.userId` / `Order.productId` in
your sample. Add security, DTO mapping, bean validation, exception handlers, and business
rules (stock checks, coupon validation, order-status transitions) on top of this scaffold
as the next step.
