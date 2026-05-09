# Instrument Shop Java Project

A console-based inventory management system for a musical instrument shop, built as a university project for my **PAOJ** course. Uses MySQL for persistence with JDBC, BCrypt for password hashing, and a layered architecture.

---

## Tech Stack

- **Java 21** — core language
- **MySQL 8** — database (via Docker)
- **JDBC** — database access (no ORM)
- **BCrypt** (`org.mindrot.jbcrypt`) — password hashing
- **Maven** — build tool

---

## Setup

### 1. Start the database

```bash
docker compose up -d
```

This starts MySQL 8 on port `3307`.

### 2. Build and run

```bash
mvn compile exec:java -Dexec.mainClass="shop.Main"
```

Or compile and run manually:

```bash
mvn package
java -jar target/proiect-shop-1.0-SNAPSHOT.jar
```

### 3. Default admin credentials

```
Username: admin
Password: 1234
```

On first run, the database tables are created automatically and the admin user is seeded.

---

## Architecture

```
src/main/java/shop/
├── Main.java                   # Entry point
├── config/
│   ├── ConfigLoader.java       # Reads application.properties
│   ├── DatabaseConnection.java # JDBC connection helper
│   └── DatabaseInitializer.java# Runs schema.sql at startup
├── data/
│   └── StoreInventory.java    # Singleton facade — all business logic
├── enums/                     # Enumerations (pickup types, roles, etc.)
├── interfaces/
│   └── Tunable.java           # Interface for tunable instruments
├── models/                    # POJOs (Product, Guitar, User, Partner, etc.)
├── repository/                # JDBC DAOs (CRUD per entity)
├── services/
│   ├── ConsoleUi.java         # CLI menu loop
│   ├── CSVFileService.java    # Exports sales to CSV
│   ├── ProductFactory.java    # Product construction helpers
│   └── PartnerFactory.java    # Partner construction helpers
└── util/
    └── SearchUtils.java       # Levenshtein distance for fuzzy search
```

### Layers

- **Models** — plain data classes with no logic beyond validation
- **Repository** — raw JDBC, each method opens its own connection
- **Services** — factories and UI orchestration
- **StoreInventory** — the single entry point for all operations, manages in-memory caches and delegates to repositories

---

## Features

### Product Management
- Add / remove / update products (Guitar, Bass, Drumset, Keyboard, Amplifier, Accessory)
- Filter by category (fuzzy match via Levenshtein distance)
- Filter by price range
- Low-stock report
- Bulk discount by category
- Inventory valuation

### Sales
- Register partners (wholesale buyers with discount rates)
- Start / close sale periods
- Sell products to partners (auto-calculates discounted price)
- View current period and history
- Export current period to CSV

### User System
- Login / register with BCrypt-hashed passwords
- Role-based access: **ADMIN** and **EMPLOYEE**
- Fine-grained permissions: `MANAGE_USERS`, `MANAGE_PRODUCTS`, `MANAGE_PARTNERS`
- ADMINS can manage other users and their permissions

---

## **List of ~12 possible actions:**
- Add product to list of products for sale
- Remove a product from the list of products for sale
- Apply a bulk discount
- Generate low-stock report
- View inventory valuation
- Register shop partner
- Remove shop partner
- Sell a product to a partner 
- Update the information of a product
- Filter products 
- Start a new sale period 
- End a sale period
- Get information about a sale period
- Create CSV file with sale information about a period

## **List of ~10 entities**
- Guitar
- Amplifier
- Drumset
- Keyboard
- Piano
- Bass
- Accessory
- Partner
- SaleRecord
- SalePeriod
- StoreInventory
- User

## **Entity Relations**
- Inheritance for instruments:
    - `Superclass`: Product
    - `Subclasses`: Guitar, Amplifier, Drumset, Keyboard, Piano, Bass
- Composition for sales:
    - **SalePeriod** has many **SaleRecords**
    - **SaleRecord** has one **Product** and one **Partner**
    - **StoreInventory** has many **Products**

---

## Database Schema

All primary keys use `INT AUTO_INCREMENT`. Display IDs (e.g. `PRD-000001`, `USR-000001`) are formatted in Java for console output only.

| Table | Purpose |
|---|---|
| `products` | Base product table (joined by subtypes) |
| `guitars`, `basses`, `drumsets`, `keyboards`, `amplifiers`, `accessories` | Product subtypes, FK → `products(id)` |
| `users` | App users with BCrypt-hashed passwords |
| `user_permissions` | Many-to-many user permissions |
| `partners` | Wholesale buyers with discount rates |
| `sale_periods` | Sales periods (only one open at a time) |
| `sale_records` | Individual transactions within a period |
