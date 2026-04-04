# MRP System Test Plan

## 1. System Overview
- **Application**: Spring Boot 3.x MRP System
- **Database**: MySQL (localhost:3306)
- **Message Queue**: RocketMQ 5.3.3 (localhost:9876)
- **API Prefix**: /api/v1
- **Health Endpoint**: /actuator/health

## 2. Test Scenarios

### 2.1 Application Startup Tests
- **Test 1.1**: Application starts successfully
- **Test 1.2**: RocketMQ connection validation
- **Test 1.3**: Topic creation
- **Test 1.4**: Health check endpoint

### 2.2 API Tests
- **Test 2.1**: BOM API
- **Test 2.2**: Inventory API
- **Test 2.3**: Order API
- **Test 2.4**: MRP API
- **Test 2.5**: Integration API
- **Test 2.6**: Reconciliation API

### 2.3 RocketMQ Tests
- **Test 3.1**: Event publishing
- **Test 3.2**: Event consumption
- **Test 3.3**: Message content verification

### 2.4 End-to-End Tests
- **Test 4.1**: Order → MRP → Production Flow
- **Test 4.2**: External System Integration Flow
- **Test 4.3**: Reconciliation Process

### 2.5 Failure Scenarios
- **Test 5.1**: RocketMQ unavailable at startup
- **Test 5.2**: Database connection failure
- **Test 5.3**: Invalid API requests
- **Test 5.4**: Message publishing failure

## 3. Test Data

### 3.1 Materials
```json
[
  {
    "materialCode": "MAT-001",
    "materialName": "Cotton Fabric",
    "category": "Fabric",
    "unit": "meter",
    "price": 10.0,
    "leadTime": 7,
    "status": "ACTIVE"
  },
  {
    "materialCode": "MAT-002",
    "materialName": "Thread",
    "category": "Accessories",
    "unit": "spool",
    "price": 5.0,
    "leadTime": 3,
    "status": "ACTIVE"
  },
  {
    "materialCode": "MAT-003",
    "materialName": "Button",
    "category": "Accessories",
    "unit": "piece",
    "price": 0.5,
    "leadTime": 5,
    "status": "ACTIVE"
  }
]
```

### 3.2 BOMs
```json
[
  {
    "productCode": "PROD-001",
    "materialCode": "MAT-001",
    "quantity": 2.0,
    "unit": "meter"
  },
  {
    "productCode": "PROD-001",
    "materialCode": "MAT-002",
    "quantity": 1.0,
    "unit": "spool"
  },
  {
    "productCode": "PROD-001",
    "materialCode": "MAT-003",
    "quantity": 5.0,
    "unit": "piece"
  }
]
```

### 3.3 Inventory
```json
[
  {
    "materialCode": "MAT-001",
    "materialName": "Cotton Fabric",
    "quantity": 100.0,
    "unit": "meter",
    "cost": 10.0
  },
  {
    "materialCode": "MAT-002",
    "materialName": "Thread",
    "quantity": 50.0,
    "unit": "spool",
    "cost": 5.0
  },
  {
    "materialCode": "MAT-003",
    "materialName": "Button",
    "quantity": 500.0,
    "unit": "piece",
    "cost": 0.5
  }
]
```

### 3.4 Orders
```json
[
  {
    "orderCode": "ORD-001",
    "customerId": "CUST-001",
    "designId": "PROD-001",
    "quantity": 10,
    "deliveryDate": "2026-04-20",
    "status": "CREATED"
  },
  {
    "orderCode": "ORD-002",
    "customerId": "CUST-002",
    "designId": "PROD-001",
    "quantity": 5,
    "deliveryDate": "2026-04-15",
    "status": "CREATED"
  }
]
```

## 4. Test Scripts

### 4.1 Health Check Test
```bash
# Test health endpoint
curl -X GET http://localhost:8080/actuator/health

# Expected response:
# {
#   "status": "UP",
#   "components": {
#     "rocketMQ": {
#       "status": "UP",
#       "details": {
#         "nameServer": "localhost:9876",
#         "brokerCount": 1,
#         "clusterCount": 1,
#         "status": "connected"
#       }
#     },
#     "db": {
#       "status": "UP",
#       "details": {
#         "database": "MySQL",
#         "validationQuery": "isValid()"
#       }
#     }
#   }
# }
```

### 4.2 API Tests

#### BOM API
```bash
# Get all BOMs
curl -X GET http://localhost:8080/api/v1/boms

# Create BOM
curl -X POST http://localhost:8080/api/v1/boms \
  -H "Content-Type: application/json" \
  -d '{"productCode": "PROD-002", "materialCode": "MAT-001", "quantity": 3.0, "unit": "meter"}'
```

#### Inventory API
```bash
# Get all inventory
curl -X GET http://localhost:8080/api/v1/inventory

# Get inventory by material code
curl -X GET http://localhost:8080/api/v1/inventory/MAT-001
```

#### Order API
```bash
# Create order
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"orderCode": "ORD-003", "customerId": "CUST-003", "designId": "PROD-001", "quantity": 15, "deliveryDate": "2026-04-25", "status": "CREATED"}'

# Get all orders
curl -X GET http://localhost:8080/api/v1/orders
```

#### MRP API
```bash
# Run MRP calculation
curl -X POST http://localhost:8080/api/v1/mrp/calculate

# Expected response should include material requirements
```

### 4.3 RocketMQ Tests

#### Message Publishing Test
```bash
# Publish test message
curl -X POST http://localhost:8080/api/v1/message/publish/orders-events \
  -d "message=Test order event"

# Publish order event
curl -X POST http://localhost:8080/api/v1/message/publish/order
```

## 5. Test Execution

### 5.1 Prerequisites
1. Start MySQL server (localhost:3306)
2. Start RocketMQ NameServer (localhost:9876)
3. Start RocketMQ Broker

### 5.2 Execution Steps
1. Build and run the application
2. Run health check test
3. Run API tests
4. Run RocketMQ tests
5. Run end-to-end tests
6. Run failure scenarios

## 6. Validation Criteria

- **Application Startup**: Should start within 30 seconds
- **RocketMQ Connection**: Should connect successfully
- **API Responses**: Should return 200 OK for valid requests
- **RocketMQ Messages**: Should be published and consumed successfully
- **Database Operations**: Should create and update records correctly
- **MRP Calculation**: Should generate accurate material requirements

## 7. Failure Handling

- **RocketMQ Unavailable**: Application should fail to start with clear error message
- **Database Failure**: Application should fail to start with clear error message
- **Invalid API Requests**: Should return appropriate error codes
- **Message Publishing Failure**: Should log error but continue operation

## 8. Test Results

| Test Case | Expected Result | Actual Result | Status |
|-----------|----------------|---------------|--------|
| 1.1 Application Startup | Success | TBD | Pending |
| 1.2 RocketMQ Connection | Success | TBD | Pending |
| 1.3 Topic Creation | Success | TBD | Pending |
| 1.4 Health Check | UP | TBD | Pending |
| 2.1 BOM API | Success | TBD | Pending |
| 2.2 Inventory API | Success | TBD | Pending |
| 2.3 Order API | Success | TBD | Pending |
| 2.4 MRP API | Success | TBD | Pending |
| 3.1 Event Publishing | Success | TBD | Pending |
| 3.2 Event Consumption | Success | TBD | Pending |
| 4.1 Order → MRP → Production | Success | TBD | Pending |
| 5.1 RocketMQ Unavailable | Failure | TBD | Pending |
| 5.2 Database Failure | Failure | TBD | Pending |
