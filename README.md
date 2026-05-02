# E-Commerce Backend API with AI Features

A production-ready e-commerce REST API built with Spring Boot that goes beyond basic CRUD — it includes a complete shopping workflow from cart to order, intelligent product recommendations powered by collaborative filtering, demand-based dynamic pricing, and Google Gemini AI integration for automated product descriptions.

This project was built to demonstrate real-world backend engineering concepts including transactional integrity, layered architecture, and AI/ML integration in a Java Spring Boot environment.

---

## What This Project Does

Most student projects stop at basic Create, Read, Update, Delete operations. This one simulates how a real e-commerce backend actually works:

- A customer browses products filtered by category
- They add items to their personal cart
- When they place an order, the system atomically converts their cart into an order, deducts stock quantities, locks in prices at the time of purchase, and clears the cart — all in a single database transaction that rolls back completely if anything goes wrong
- The AI recommendation engine analyzes historical order data to suggest products that other customers bought together
- The dynamic pricing engine monitors stock levels and order velocity to suggest optimal pricing
- Google Gemini AI automatically writes professional product descriptions

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| ORM | Spring Data JPA + Hibernate 7 |
| Database | MySQL 8.0 |
| Build Tool | Maven |
| API Testing | Postman |
| AI Integration | Google Gemini API |
| Version Control | Git + GitHub |

---

## Project Architecture

The project follows a strict three-layer architecture:
Controller Layer  →  receives HTTP requests, returns JSON responses
Service Layer     →  contains all business logic
Repository Layer  →  handles all database operations via JPA

This separation means the controller never talks directly to the database, and the repository never contains business rules. Each layer has a single responsibility.

---

## Database Design

The system has 7 entities with carefully designed relationships:
User ──────────── Cart (One-to-One)
Cart ──────────── CartItem (One-to-Many)
CartItem ──────── Product (Many-to-One)
Product ───────── Category (Many-to-One)
User ──────────── Order (One-to-Many)
Order ─────────── OrderItem (One-to-Many)
OrderItem ──────── Product (Many-to-One)

A notable design decision: OrderItem stores priceAtOrder — the product's price at the time of purchase. This means price changes never retroactively affect order history, which is how real e-commerce systems work.

---

## AI and ML Features

### 1. Collaborative Filtering Recommendations

When a user views a product, the system finds all historical orders that contained that product, collects every other product in those orders, and ranks them by co-purchase frequency. The top 5 results are returned as recommendations.

This is item-based collaborative filtering — the same family of algorithms that powers Amazon's "Customers also bought" section. It is implemented using a nested JPQL query directly on the order history data, with no external ML library required.

Endpoint: GET /api/ai/products/{id}/recommendations

### 2. Demand-Based Dynamic Pricing

The pricing engine evaluates two signals for each product — current stock quantity and total order count. Based on these signals it recommends a price adjustment:

- Low stock + high demand → suggest 15% price increase
- High stock + low demand → suggest 10% price decrease
- Critical stock + very high demand → suggest 20% price increase
- Otherwise → price is optimal

  Endpoint: GET /api/ai/products/{id}/pricing
  
  ### 3. Gemini LLM Integration

When triggered, the system sends a structured prompt to Google's Gemini API describing the product name and category. The returned description is automatically saved to the product record in the database.
Endpoint: POST /api/ai/products/{id}/describe

---

## The Order Placement Flow

This is the most technically significant part of the project. The placeOrder method in OrderServiceImpl is annotated with @Transactional, which means every database operation inside it either succeeds completely or rolls back completely.

Here is what happens when a user places an order:

1. Fetch the user's cart and validate it is not empty
2. Check stock availability for every item in the cart — if any product has insufficient stock, throw an exception and roll back
3. Create a new Order record with status PLACED and the current timestamp
4. For each cart item, create an OrderItem with the product, quantity, and current price locked in
5. Deduct the ordered quantity from each product's stock
6. Save the order which cascades to save all order items
7. Clear the cart using orphanRemoval

If the application crashes between steps 5 and 6, the entire transaction rolls back — no phantom stock deductions, no incomplete orders.

---

## API Reference

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/users | Register a new user |
| GET | /api/users | Get all users |
| GET | /api/users/{id} | Get user by ID |
| PUT | /api/users/{id} | Update user |
| DELETE | /api/users/{id} | Delete user |

### Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/categories | Create category |
| GET | /api/categories | Get all categories |
| GET | /api/categories/{id} | Get by ID |
| PUT | /api/categories/{id} | Update category |
| DELETE | /api/categories/{id} | Delete category |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/products | Create product |
| GET | /api/products | Get all products |
| GET | /api/products/{id} | Get by ID |
| PUT | /api/products/{id} | Update product |
| DELETE | /api/products/{id} | Delete product |
| GET | /api/products/search?name= | Search by name |
| GET | /api/products/category/{name} | Filter by category |

### Cart
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/cart/{userId} | View cart |
| POST | /api/cart/{userId}/add/{productId} | Add to cart |
| PUT | /api/cart/{userId}/update/{cartItemId} | Update quantity |
| DELETE | /api/cart/{userId}/remove/{cartItemId} | Remove item |
| DELETE | /api/cart/{userId}/clear | Clear cart |

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/orders/{userId}/place | Place order |
| GET | /api/orders/{userId} | Order history |
| GET | /api/orders/detail/{orderId} | Order details |
| PUT | /api/orders/{orderId}/status | Update status |

### AI Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/ai/products/{id}/describe | Generate AI description |
| GET | /api/ai/products/{id}/recommendations | Get recommendations |
| GET | /api/ai/products/{id}/pricing | Get dynamic price suggestion |

---

## Running the Project Locally

### Prerequisites
- Java 21 or higher
- MySQL 8.0
- Maven

### Steps

1. Clone the repository

```bash
git clone https://github.com/samzuiiii/ecommerce-springboot-api.git
cd ecommerce-springboot-api
```

2. Create a MySQL database

```sql
CREATE DATABASE ecommercedb;
```

3. Update src/main/resources/application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommercedb?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
gemini.api.key=your_gemini_api_key
```

4. Run the application

```bash
./mvnw spring-boot:run
```

5. The API will be available at http://localhost:8080

---

## What I Learned

Building this project gave me hands-on experience with layered Spring Boot architecture, JPA entity relationships, transactional database operations, REST API design principles, and integrating external AI APIs into a backend system. The collaborative filtering implementation in particular was a deep dive into how recommendation systems work at a conceptual level and how to implement them efficiently using JPQL.

---

## Author

**Samyuktha V**
B.Tech Information Technology — Sri Krishna College of Engineering and Technology, Coimbatore
[LinkedIn](www.linkedin.com/in/samyuktha-v-33994032b)| [GitHub](https://github.com/samzuiiii)
