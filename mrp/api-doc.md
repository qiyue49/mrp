# SparkIt MRP系统 API接口文档

## 1. 订单管理接口

| 路径 | 请求方式 | 功能描述 | 请求参数 | 成功响应 | 失败响应 |
|------|---------|----------|----------|----------|----------|
| /api/orders | POST | 创建订单 | orderCode: string<br>customerId: string<br>designId: string<br>quantity: integer<br>deliveryDate: string | `{"code": 200, "message": "success", "data": Order对象}` | `{"code": 400, "message": "参数错误"}` |
| /api/orders/{id} | GET | 获取订单详情 | id: string | `{"code": 200, "message": "success", "data": Order对象}` | `{"code": 500, "message": "订单不存在"}` |
| /api/orders/{id}/status | PUT | 更新订单状态 | id: string<br>status: string<br>updatedBy: string | `{"code": 200, "message": "success", "data": Order对象}` | `{"code": 500, "message": "订单不存在"}` |
| /api/orders/code/{code} | GET | 根据订单编码获取订单 | code: string | `{"code": 200, "message": "success", "data": Order对象}` | `{"code": 500, "message": "订单不存在"}` |

## 2. 集成管理接口

| 路径 | 请求方式 | 功能描述 | 请求参数 | 成功响应 | 失败响应 |
|------|---------|----------|----------|----------|----------|
| /api/integration/systems | POST | 注册外部系统 | systemCode: string<br>systemName: string<br>systemType: string<br>connectionInfo: object | `{"code": 200, "message": "success", "data": ExternalSystem对象}` | `{"code": 400, "message": "参数错误"}` |
| /api/integration/systems/{id} | GET | 获取外部系统详情 | id: string | `{"code": 200, "message": "success", "data": ExternalSystem对象}` | `{"code": 500, "message": "系统不存在"}` |
| /api/integration/mappings | POST | 创建外部ID映射 | systemId: string<br>entityType: string<br>internalId: string<br>externalId: string | `{"code": 200, "message": "success", "data": ExternalSystemMapping对象}` | `{"code": 400, "message": "参数错误"}` |
| /api/integration/mappings/internal | GET | 根据内部ID获取映射 | systemId: string<br>entityType: string<br>internalId: string | `{"code": 200, "message": "success", "data": ExternalSystemMapping对象}` | `{"code": 500, "message": "映射不存在"}` |
| /api/integration/mappings/external | GET | 根据外部ID获取映射 | systemId: string<br>entityType: string<br>externalId: string | `{"code": 200, "message": "success", "data": ExternalSystemMapping对象}` | `{"code": 500, "message": "映射不存在"}` |

## 3. 对账管理接口

| 路径 | 请求方式 | 功能描述 | 请求参数 | 成功响应 | 失败响应 |
|------|---------|----------|----------|----------|----------|
| /api/reconciliation/batches | POST | 创建对账批次 | batchCode: string<br>reconciliationType: string<br>startDate: string<br>endDate: string | `{"code": 200, "message": "success", "data": ReconciliationBatch对象}` | `{"code": 400, "message": "参数错误"}` |
| /api/reconciliation/batches/{id} | GET | 获取对账批次详情 | id: string | `{"code": 200, "message": "success", "data": ReconciliationBatch对象}` | `{"code": 500, "message": "批次不存在"}` |
| /api/reconciliation/details | POST | 创建对账明细 | batchId: string<br>entityType: string<br>internalId: string<br>externalId: string<br>internalValue: object<br>externalValue: object<br>status: string<br>differenceReason: string | `{"code": 200, "message": "success", "data": ReconciliationDetail对象}` | `{"code": 400, "message": "参数错误"}` |
| /api/reconciliation/batches/{id}/detail | POST | 执行明细对账 | id: string | `{"code": 200, "message": "success"}` | `{"code": 500, "message": "执行失败"}` |
| /api/reconciliation/batches/{id}/summary | POST | 执行汇总对账 | id: string | `{"code": 200, "message": "success"}` | `{"code": 500, "message": "执行失败"}` |
| /api/reconciliation/batches/{id}/physical | POST | 执行账实对账 | id: string | `{"code": 200, "message": "success"}` | `{"code": 500, "message": "执行失败"}` |

## 4. 状态码定义

| 状态码 | 描述 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 500 | 系统错误 |

## 5. 状态枚举

### 订单状态
- CREATED: 创建
- PRICING: 定价中
- PRICED: 已定价
- CONFIRMED: 已确认
- SAMPLE_REQUESTED: 样品需求已创建
- SAMPLE_COMPLETED: 样品已完成
- PRODUCTION_PLANNED: 生产计划已创建
- PRODUCING: 生产中
- PRODUCED: 生产完成
- QC_PASSED: QC通过
- SHIPPING: 运输中
- DELIVERED: 已送达
- COMPLETED: 已完成
- CANCELLED: 已取消

### 样品状态
- REQUESTED: 样品需求已创建
- ASSIGNED: 样品任务已分配
- IN_PROGRESS: 样品制作中
- COMPLETED: 样品已完成
- APPROVED: 样品已批准
- REJECTED: 样品已拒绝

### 生产任务状态
- CREATED: 生产任务已创建
- ASSIGNED: 生产任务已分配
- STARTED: 生产任务已开始
- IN_PROGRESS: 生产任务进行中
- COMPLETED: 生产任务已完成
- QC_PENDING: 等待QC
- QC_PASSED: QC通过
- QC_REJECTED: QC拒绝
- REWORKING: 返工中
- CANCELLED: 已取消

### 物流状态
- CREATED: 物流任务创建
- IN_TRANSIT: 物流运输中
- DELIVERED: 物流送达
- COMPLETED: 物流完成
