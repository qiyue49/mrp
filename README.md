# MRP System

A comprehensive Material Requirements Planning (MRP) system built with Spring Boot 3 and a modern Vue.js frontend. Designed for manufacturing and production planning with full order lifecycle management.

## Overview

This MRP system provides end-to-end management of manufacturing operations including order management, production planning, inventory control, bill of materials (BOM), quality control, logistics tracking, and external system integration.

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Java Version**: Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA with Hibernate
- **Message Queue**: Apache RocketMQ 5.3.3
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Build Tool**: Maven

### Frontend
- **Framework**: Vue.js 3 with Vite
- **UI Components**: Element Plus
- **State Management**: Pinia
- **Routing**: Vue Router
- **HTTP Client**: Axios
- **Build Output**: Static files served by Spring Boot

## Features

### Core Modules

1. **Order Management**
   - Create and manage customer orders
   - Order status tracking (Pending, Confirmed, In Production, Shipped, Delivered)
   - Order code-based lookup

2. **Bill of Materials (BOM)**
   - Define product structures
   - Material requirements calculation
   - Product code-based BOM retrieval

3. **Inventory Management**
   - Real-time inventory tracking
   - Material-based inventory queries
   - Stock level monitoring

4. **Production Planning**
   - Production task scheduling
   - Capacity planning
   - Production workflow management

5. **Quality Control (QC)**
   - QC record management
   - Quality inspection tracking

6. **Logistics Management**
   - Shipment tracking
   - Delivery status updates

7. **External System Integration**
   - External system registration
   - ID mapping for cross-system data synchronization
   - Integration logging

8. **Reconciliation Management**
   - Batch reconciliation processing
   - Financial reconciliation tracking

### Event-Driven Architecture
The system uses Apache RocketMQ for asynchronous event processing:
- Order events (Created, Confirmed)
- Production events (Planned, Task Created)
- Sample events (Requested)

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0** (running on localhost:3306)
- **Apache RocketMQ** (optional, for event-driven features)

## Quick Start

### 1. Database Setup

Ensure MySQL is running and create the database:

```sql
CREATE DATABASE IF NOT EXISTS mrp_system;
```

Default database configuration:
- URL: `jdbc:mysql://localhost:3306/mrp_system`
- Username: `root`
- Password: `root`

### 2. Start the Application

**Option A: Run via Maven**
```bash
cd mrp
mvn spring-boot:run
```

**Option B: Build and Run JAR**
```bash
cd mrp
mvn clean package -DskipTests
java -jar target/mrp-system-1.0.0.jar
```

**Option C: Run via IDE**
- Open the project in IntelliJ IDEA or Eclipse
- Run `MrpSystemApplication.java`

### 3. Access the Application

- **Web Application**: http://localhost:8080
- **API Base URL**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html
- **API Docs**: http://localhost:8080/api/v1/api-docs

## API Endpoints

### Order Management
- `POST /api/v1/orders` - Create new order
- `GET /api/v1/orders` - List all orders
- `GET /api/v1/orders/{id}` - Get order by ID
- `GET /api/v1/orders/code/{code}` - Get order by code
- `PUT /api/v1/orders/{id}/status` - Update order status

### Inventory Management
- `POST /api/v1/inventory` - Create inventory record
- `GET /api/v1/inventory` - List all inventory
- `GET /api/v1/inventory/material/{materialCode}` - Get inventory by material
- `PUT /api/v1/inventory/{id}` - Update inventory
- `DELETE /api/v1/inventory/{id}` - Delete inventory

### BOM Management
- `POST /api/v1/bom` - Create BOM
- `GET /api/v1/bom` - List all BOMs
- `GET /api/v1/bom/product/{productCode}` - Get BOM by product
- `PUT /api/v1/bom/{id}` - Update BOM
- `DELETE /api/v1/bom/{id}` - Delete BOM

### MRP Calculation
- `POST /api/v1/mrp/calculate` - Run MRP calculation

### Integration Management
- External system registration and configuration
- ID mapping management
- Integration log monitoring

### Reconciliation Management
- Reconciliation batch operations
- Detail tracking and reporting

## Project Structure

```
mrp/
├── src/
│   ├── main/
│   │   ├── java/com/sparkit/mrp/
│   │   │   ├── MrpSystemApplication.java    # Application entry point
│   │   │   ├── common/                      # Common utilities and handlers
│   │   │   ├── config/                      # Configuration classes
│   │   │   ├── controller/                  # REST API controllers
│   │   │   ├── data/                        # Data initialization
│   │   │   ├── dto/                         # Data transfer objects
│   │   │   ├── entity/                      # JPA entities
│   │   │   ├── event/                       # Event classes (order, production, sample)
│   │   │   ├── health/                      # Health indicators
│   │   │   ├── repository/                  # JPA repositories
│   │   │   ├── service/                     # Business logic services
│   │   │   └── util/                        # Utility classes
│   │   └── resources/
│   │       ├── static/                      # Frontend static files
│   │       │   └── index.html               # Main frontend entry
│   │       └── application.yml              # Application configuration
│   └── test/                                # Unit and integration tests
├── pom.xml                                  # Maven configuration
└── README.md                                # This file
```

## Configuration

### Application Properties (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mrp_system
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: create  # Auto-create tables (use 'update' in production)

rocketmq:
  name-server: localhost:9876
  producer:
    group: mrp-producer-group

server:
  port: 8080
```

### Database Entities

- **Order** - Customer orders
- **Design** - Product designs
- **Sample** - Sample management
- **ProductionPlan** - Production planning
- **ProductionTask** - Production tasks
- **QcRecord** - Quality control records
- **Logistics** - Shipment tracking
- **Inventory** - Stock management
- **BOM** - Bill of materials
- **Material** - Material master data
- **ExternalSystem** - External system configuration
- **ExternalSystemMapping** - ID mapping
- **IntegrationLog** - Integration logs
- **ReconciliationBatch** - Reconciliation batches
- **ReconciliationDetail** - Reconciliation details

## Testing

Run unit and integration tests:

```bash
cd mrp
mvn test
```

Test classes include:
- `OrderApiTest` - Order API tests
- `InventoryApiTest` - Inventory API tests
- `BomApiTest` - BOM API tests
- `MrpApiTest` - MRP calculation tests
- `HealthCheckTest` - Health check tests
- `ApplicationStartupTest` - Application startup tests
- `EndToEndTest` - End-to-end tests

## Monitoring & Health Checks

The application exposes actuator endpoints:
- `/actuator/health` - Health status
- `/actuator/info` - Application info
- `/actuator/metrics` - Metrics data

RocketMQ health indicator is included for message queue monitoring.

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running: `net start mysql` (Windows) or `sudo systemctl start mysql` (Linux)
- Check credentials in `application.yml`
- Ensure database `mrp_system` exists

### RocketMQ Connection (Optional)
- If RocketMQ is not running, the application will still start but event-driven features will be disabled
- Ensure RocketMQ name server is accessible at `localhost:9876`
- Check RocketMQ broker status

### Port Conflicts
- Default port is 8080 - ensure it's not in use by another application
- Change port in `application.yml` if needed: `server.port: 8081`

### Build Issues
- Ensure Maven is properly installed: `mvn -version`
- Clear Maven cache: `mvn clean`
- Skip tests during build: `mvn clean package -DskipTests`

## Development

### Adding New Features
1. Create entity class in `entity/` package
2. Create repository interface in `repository/` package
3. Create service class in `service/` package
4. Create controller class in `controller/` package
5. Add tests in `src/test/java/`

### Event Publishing
To publish events to RocketMQ:
```java
@Autowired
private EventPublisher eventPublisher;

eventPublisher.publish(new OrderCreatedEvent(order));
```

## License

MIT License - See LICENSE file for details

## Support

For issues and feature requests, please contact the development team.

---

**Note**: This application auto-creates database tables on startup. For production use, change `spring.jpa.hibernate.ddl-auto` from `create` to `update` or `validate` in `application.yml`.