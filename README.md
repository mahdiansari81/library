# 📚 Library Management System

A RESTful API for managing a library system, built with **Spring Boot 4**, **PostgreSQL**.

---

## 🎯 Overview

This project is a **library management system** that allows users to:

* Manage books (create, read, update, delete)
* Borrow and return books
* Manage users
* Search and filter books dynamically
* Handle **concurrent borrow requests** safely

The project focuses on solving **real-world challenges** like:

* **Concurrency** (multiple users borrowing the same book)
* **Dynamic filtering & pagination**
* **Global exception handling**

---

## ✨ Features

### 📖 Book Management

* ✅ Create, read, update, delete books
* ✅ Search by title or author (case-insensitive, partial match)
* ✅ Filter by publish year range
* ✅ Filter by availability
* ✅ Pagination & sorting

### 📚 Borrow Management

* ✅ Borrow a book (with concurrency control)
* ✅ Return a book
* ✅ Track borrow history per user
* ✅ Unique trace code for each borrow

### 👤 User Management

* ✅ Create users
* ✅ Get user by ID
* ✅ List all users (with pagination)
* ✅ Password hashing with BCrypt

### 🚨 Exception Handling

* ✅ Global exception handler
* ✅ Custom exceptions (`ResourceNotFoundException`, `DuplicateResourceException`, `ConcurrencyException`)
* ✅ Standardized error responses

### 🌱 Data Seeding

* ✅ 100 sample books added on startup
* ✅ Default user created automatically

---

## 🚀 Getting Started

### Prerequisites

* **Java 25+**
* **Maven 3.8+**
* **PostgreSQL 14+**

### ▶️ Running the Project

#### 1. Clone the Repository

```bash
git clone https://github.com/mahdiansari81/library.git
cd library
```

#### 2. Create the PostgreSQL Database

Create a PostgreSQL database for the project.

For example:

```sql
CREATE DATABASE library;
```

Make sure PostgreSQL is running before starting the application.

#### 3. Configure Database Connection

Open the project's configuration file:

```text
src/main/resources/application.properties
```

Configure your PostgreSQL connection:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Replace `YOUR_USERNAME` and `YOUR_PASSWORD` with your PostgreSQL credentials.

#### 4. Run the Project with Maven

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

If Maven is installed globally, you can also run:

```bash
mvn spring-boot:run
```

#### 5. Run the Project from IntelliJ IDEA

You can also run the application directly from IntelliJ IDEA:

1. Open the project in **IntelliJ IDEA**.
2. Make sure the project uses **Java 25+**.
3. Make sure PostgreSQL is running.
4. Open the main Spring Boot application class.
5. Click the **Run ▶** button next to the `main` method.

After the application starts successfully, the API will be available at:

```text
http://localhost:8080
```

The application will automatically initialize the required data, including **100 sample books** and a **default user**.
