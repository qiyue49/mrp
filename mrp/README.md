# MRP System

A complete Material Requirements Planning (MRP) system with Spring Boot backend and Vue3 frontend, designed to run 100% locally without Docker or cloud services.

## Features

- **Backend**: Spring Boot 3 + MySQL
- **Frontend**: Vue3 (single HTML file, no build tools)
- **Full MRP functionality**: Login, Dashboard, BOM, Sales Orders, MRP Calculation, Inventory
- **Local-only**: No Docker, no containers, no cloud services
- **Auto-created database tables with test data**

## Prerequisites

1. **Java 17** installed
2. **Maven** installed
3. **MySQL** installed and running on localhost:3306
4. **MySQL credentials**: root/root

## 3-Step Local Startup Guide

### Step 1: Start Local MySQL

Ensure MySQL is running on localhost:3306 with root/root credentials.

### Step 2: Run Backend

**Option 1: Run via IDE**
- Open the project in your IDE
- Run the `MrpSystemApplication.java` main class

**Option 2: Run via Maven**
```bash
mvn spring-boot:run
```

The backend will start on port 8080 and automatically create the `sparkit_mrp` database and tables with test data.

### Step 3: Run Frontend

- Double-click `index.html` in the project root
- The frontend will open in your default browser
- Use any username/password to login

## API Endpoints

- **Orders**: http://localhost:8080/api/v1/orders
- **BOM**: http://localhost:8080/api/v1/bom
- **Inventory**: http://localhost:8080/api/v1/inventory
- **MRP Calculation**: http://localhost:8080/api/v1/mrp/calculate
- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html

## Project Structure

```
mrp/
├── src/
│   ├── main/
│   │   ├── java/com/sparkit/mrp/
│   │   │   ├── controller/      # API controllers
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── service/         # Business logic
│   │   │   ├── data/            # Data initialization
│   │   │   └── MrpSystemApplication.java  # Main class
│   │   └── resources/
│   │       └── application.yml  # Application configuration
├── index.html      # Vue3 frontend
├── pom.xml         # Maven configuration
└── README.md       # This file
```

## Troubleshooting

### MySQL Not Running
If MySQL is not running, start it using:
```bash
# Windows
net start mysql

# Linux
sudo systemctl start mysql
```

### Database Connection Issues
Ensure MySQL is configured to accept connections on localhost:3306 with root/root credentials.

### Frontend Not Loading Data
If the backend is not running, the frontend will use mock data automatically.

## License

MIT
