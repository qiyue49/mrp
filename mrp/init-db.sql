-- 初始化数据库
CREATE DATABASE IF NOT EXISTS sparkit_mrp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sparkit_mrp;

-- 物料分类表
CREATE TABLE IF NOT EXISTS `material_category` (
  `category_id` VARCHAR(36) NOT NULL COMMENT '分类ID',
  `category_code` VARCHAR(50) NOT NULL COMMENT '分类编码',
  `category_name` VARCHAR(100) NOT NULL COMMENT '分类名称',
  `description` TEXT COMMENT '分类描述',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `uk_category_code` (`category_code`),
  KEY `idx_status` (`status`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物料分类表';

-- 物料表
CREATE TABLE IF NOT EXISTS `material` (
  `material_id` VARCHAR(36) NOT NULL COMMENT '物料ID',
  `material_code` VARCHAR(50) NOT NULL COMMENT '物料编码',
  `material_name` VARCHAR(100) NOT NULL COMMENT '物料名称',
  `category_id` VARCHAR(36) NOT NULL COMMENT '分类ID',
  `mill_id` VARCHAR(36) COMMENT '面料厂ID',
  `specification` JSON COMMENT '规格参数',
  `unit` VARCHAR(20) NOT NULL COMMENT '单位',
  `price` DECIMAL(18,4) COMMENT '价格',
  `currency` VARCHAR(3) NOT NULL DEFAULT 'CNY' COMMENT '币种',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`material_id`),
  UNIQUE KEY `uk_material_code` (`material_code`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_mill_id` (`mill_id`),
  KEY `idx_status` (`status`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物料表';

-- 工厂表
CREATE TABLE IF NOT EXISTS `factory` (
  `factory_id` VARCHAR(36) NOT NULL COMMENT '工厂ID',
  `factory_code` VARCHAR(50) NOT NULL COMMENT '工厂编码',
  `factory_name` VARCHAR(100) NOT NULL COMMENT '工厂名称',
  `location` VARCHAR(200) COMMENT '位置',
  `capacity` INT COMMENT '产能',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`factory_id`),
  UNIQUE KEY `uk_factory_code` (`factory_code`),
  KEY `idx_status` (`status`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工厂表';

-- 面料厂表
CREATE TABLE IF NOT EXISTS `mill` (
  `mill_id` VARCHAR(36) NOT NULL COMMENT '面料厂ID',
  `mill_code` VARCHAR(50) NOT NULL COMMENT '面料厂编码',
  `mill_name` VARCHAR(100) NOT NULL COMMENT '面料厂名称',
  `location` VARCHAR(200) COMMENT '位置',
  `contact_info` JSON COMMENT '联系信息',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`mill_id`),
  UNIQUE KEY `uk_mill_code` (`mill_code`),
  KEY `idx_status` (`status`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='面料厂表';

-- 工艺表
CREATE TABLE IF NOT EXISTS `process` (
  `process_id` VARCHAR(36) NOT NULL COMMENT '工艺ID',
  `process_code` VARCHAR(50) NOT NULL COMMENT '工艺编码',
  `process_name` VARCHAR(100) NOT NULL COMMENT '工艺名称',
  `category_id` VARCHAR(36) COMMENT '工艺分类ID',
  `description` TEXT COMMENT '工艺描述',
  `duration` INT COMMENT '预计时长(分钟)',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`process_id`),
  UNIQUE KEY `uk_process_code` (`process_code`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工艺表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
  `order_id` VARCHAR(36) NOT NULL COMMENT '订单ID',
  `order_code` VARCHAR(50) NOT NULL COMMENT '订单编码',
  `customer_id` VARCHAR(36) NOT NULL COMMENT '客户ID',
  `design_id` VARCHAR(36) NOT NULL COMMENT '设计ID',
  `quantity` INT NOT NULL COMMENT '数量',
  `delivery_date` DATE NOT NULL COMMENT '交付日期',
  `status` VARCHAR(20) NOT NULL DEFAULT 'CREATED' COMMENT '状态',
  `event_id` VARCHAR(36) COMMENT '事件ID',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_code` (`order_code`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_design_id` (`design_id`),
  KEY `idx_delivery_date` (`delivery_date`),
  KEY `idx_status` (`status`),
  KEY `idx_event_id` (`event_id`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 设计表
CREATE TABLE IF NOT EXISTS `design` (
  `design_id` VARCHAR(36) NOT NULL COMMENT '设计ID',
  `design_code` VARCHAR(50) NOT NULL COMMENT '设计编码',
  `creator_id` VARCHAR(36) NOT NULL COMMENT '创作者ID',
  `design_file` VARCHAR(255) COMMENT '设计文件路径',
  `specifications` JSON COMMENT '规格参数',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `event_id` VARCHAR(36) COMMENT '事件ID',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`design_id`),
  UNIQUE KEY `uk_design_code` (`design_code`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_status` (`status`),
  KEY `idx_event_id` (`event_id`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设计表';

-- 定价表
CREATE TABLE IF NOT EXISTS `price_quote` (
  `quote_id` VARCHAR(36) NOT NULL COMMENT '定价ID',
  `order_id` VARCHAR(36) NOT NULL COMMENT '订单ID',
  `price` DECIMAL(18,4) NOT NULL COMMENT '价格',
  `currency` VARCHAR(3) NOT NULL DEFAULT 'CNY' COMMENT '币种',
  `cost_breakdown` JSON COMMENT '成本明细',
  `validity_period` DATETIME NOT NULL COMMENT '有效期',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `event_id` VARCHAR(36) COMMENT '事件ID',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`quote_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_validity_period` (`validity_period`),
  KEY `idx_status` (`status`),
  KEY `idx_event_id` (`event_id`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定价表';

-- 样品表
CREATE TABLE IF NOT EXISTS `sample` (
  `sample_id` VARCHAR(36) NOT NULL COMMENT '样品ID',
  `sample_code` VARCHAR(50) NOT NULL COMMENT '样品编码',
  `order_id` VARCHAR(36) NOT NULL COMMENT '订单ID',
  `factory_id` VARCHAR(36) COMMENT '工厂ID',
  `status` VARCHAR(20) NOT NULL DEFAULT 'REQUESTED' COMMENT '状态',
  `result` JSON COMMENT '样品结果',
  `event_id` VARCHAR(36) COMMENT '事件ID',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`sample_id`),
  UNIQUE KEY `uk_sample_code` (`sample_code`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_factory_id` (`factory_id`),
  KEY `idx_status` (`status`),
  KEY `idx_event_id` (`event_id`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='样品表';

-- 生产计划表
CREATE TABLE IF NOT EXISTS `production_plan` (
  `plan_id` VARCHAR(36) NOT NULL COMMENT '计划ID',
  `plan_code` VARCHAR(50) NOT NULL COMMENT '计划编码',
  `order_id` VARCHAR(36) NOT NULL COMMENT '订单ID',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE NOT NULL COMMENT '结束日期',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PLANNED' COMMENT '状态',
  `event_id` VARCHAR(36) COMMENT '事件ID',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`plan_id`),
  UNIQUE KEY `uk_plan_code` (`plan_code`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_start_date` (`start_date`),
  KEY `idx_end_date` (`end_date`),
  KEY `idx_status` (`status`),
  KEY `idx_event_id` (`event_id`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产计划表';

-- 生产任务表
CREATE TABLE IF NOT EXISTS `production_task` (
  `task_id` VARCHAR(36) NOT NULL COMMENT '任务ID',
  `task_code` VARCHAR(50) NOT NULL COMMENT '任务编码',
  `plan_id` VARCHAR(36) NOT NULL COMMENT '计划ID',
  `factory_id` VARCHAR(36) NOT NULL COMMENT '工厂ID',
  `process_id` VARCHAR(36) NOT NULL COMMENT '工艺ID',
  `quantity` INT NOT NULL COMMENT '数量',
  `status` VARCHAR(20) NOT NULL DEFAULT 'CREATED' COMMENT '状态',
  `assigned_at` TIMESTAMP COMMENT '分配时间',
  `started_at` TIMESTAMP COMMENT '开始时间',
  `completed_at` TIMESTAMP COMMENT '完成时间',
  `event_id` VARCHAR(36) COMMENT '事件ID',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `uk_task_code` (`task_code`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_factory_id` (`factory_id`),
  KEY `idx_process_id` (`process_id`),
  KEY `idx_status` (`status`),
  KEY `idx_assigned_at` (`assigned_at`),
  KEY `idx_started_at` (`started_at`),
  KEY `idx_completed_at` (`completed_at`),
  KEY `idx_event_id` (`event_id`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产任务表';

-- QC记录表
CREATE TABLE IF NOT EXISTS `qc_record` (
  `qc_id` VARCHAR(36) NOT NULL COMMENT 'QC ID',
  `qc_code` VARCHAR(50) NOT NULL COMMENT 'QC编码',
  `task_id` VARCHAR(36) NOT NULL COMMENT '任务ID',
  `inspector_id` VARCHAR(36) NOT NULL COMMENT '检验员ID',
  `result` VARCHAR(20) NOT NULL COMMENT 'QC结果',
  `notes` TEXT COMMENT '备注',
  `event_id` VARCHAR(36) COMMENT '事件ID',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`qc_id`),
  UNIQUE KEY `uk_qc_code` (`qc_code`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_inspector_id` (`inspector_id`),
  KEY `idx_result` (`result`),
  KEY `idx_event_id` (`event_id`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='QC记录表';

-- 物流表
CREATE TABLE IF NOT EXISTS `logistics` (
  `logistics_id` VARCHAR(36) NOT NULL COMMENT '物流ID',
  `logistics_code` VARCHAR(50) NOT NULL COMMENT '物流编码',
  `order_id` VARCHAR(36) NOT NULL COMMENT '订单ID',
  `carrier` VARCHAR(100) NOT NULL COMMENT '承运商',
  `tracking_number` VARCHAR(100) COMMENT '跟踪号',
  `status` VARCHAR(20) NOT NULL DEFAULT 'CREATED' COMMENT '状态',
  `event_id` VARCHAR(36) COMMENT '事件ID',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `source_sys` VARCHAR(50) COMMENT '来源系统',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`logistics_id`),
  UNIQUE KEY `uk_logistics_code` (`logistics_code`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_carrier` (`carrier`),
  KEY `idx_tracking_number` (`tracking_number`),
  KEY `idx_status` (`status`),
  KEY `idx_event_id` (`event_id`),
  KEY `idx_source_sys` (`source_sys`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流表';

-- 外部系统表
CREATE TABLE IF NOT EXISTS `external_system` (
  `system_id` VARCHAR(36) NOT NULL COMMENT '系统ID',
  `system_code` VARCHAR(50) NOT NULL COMMENT '系统编码',
  `system_name` VARCHAR(100) NOT NULL COMMENT '系统名称',
  `system_type` VARCHAR(50) NOT NULL COMMENT '系统类型',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `connection_info` JSON COMMENT '连接信息',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`system_id`),
  UNIQUE KEY `uk_system_code` (`system_code`),
  KEY `idx_system_type` (`system_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外部系统表';

-- 外部ID映射表
CREATE TABLE IF NOT EXISTS `external_system_mapping` (
  `mapping_id` VARCHAR(36) NOT NULL COMMENT '映射ID',
  `system_id` VARCHAR(36) NOT NULL COMMENT '系统ID',
  `entity_type` VARCHAR(50) NOT NULL COMMENT '实体类型',
  `internal_id` VARCHAR(36) NOT NULL COMMENT '内部ID',
  `external_id` VARCHAR(100) NOT NULL COMMENT '外部ID',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`mapping_id`),
  KEY `idx_system_id` (`system_id`),
  KEY `idx_entity_type` (`entity_type`),
  KEY `idx_internal_id` (`internal_id`),
  KEY `idx_external_id` (`external_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外部ID映射表';

-- 工厂自定义字段表
CREATE TABLE IF NOT EXISTS `factory_custom_field` (
  `field_id` VARCHAR(36) NOT NULL COMMENT '字段ID',
  `factory_id` VARCHAR(36) NOT NULL COMMENT '工厂ID',
  `field_name` VARCHAR(50) NOT NULL COMMENT '字段名称',
  `field_type` VARCHAR(20) NOT NULL COMMENT '字段类型',
  `field_value` TEXT COMMENT '字段值',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `version` BIGINT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`field_id`),
  KEY `idx_factory_id` (`factory_id`),
  KEY `idx_field_name` (`field_name`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工厂自定义字段表';

-- 集成日志表
CREATE TABLE IF NOT EXISTS `integration_log` (
  `log_id` VARCHAR(36) NOT NULL COMMENT '日志ID',
  `system_id` VARCHAR(36) NOT NULL COMMENT '系统ID',
  `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
  `entity_type` VARCHAR(50) NOT NULL COMMENT '实体类型',
  `entity_id` VARCHAR(36) COMMENT '实体ID',
  `status` VARCHAR(20) NOT NULL COMMENT '状态',
  `error_message` TEXT COMMENT '错误信息',
  `request_data` JSON COMMENT '请求数据',
  `response_data` JSON COMMENT '响应数据',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`log_id`),
  KEY `idx_system_id` (`system_id`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_entity_type` (`entity_type`),
  KEY `idx_entity_id` (`entity_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='集成日志表';

-- 对账批次表
CREATE TABLE IF NOT EXISTS `reconciliation_batch` (
  `batch_id` VARCHAR(36) NOT NULL COMMENT '批次ID',
  `batch_code` VARCHAR(50) NOT NULL COMMENT '批次编码',
  `reconciliation_type` VARCHAR(50) NOT NULL COMMENT '对账类型',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE NOT NULL COMMENT '结束日期',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态',
  `total_records` INT COMMENT '总记录数',
  `matched_records` INT COMMENT '匹配记录数',
  `unmatched_records` INT COMMENT '未匹配记录数',
  `created_by` VARCHAR(50) NOT NULL COMMENT '创建人',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` VARCHAR(50) COMMENT '更新人',
  `updated_at` TIMESTAMP COMMENT '更新时间',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`batch_id`),
  UNIQUE KEY `uk_batch_code` (`batch_code`),
  KEY `idx_reconciliation_type` (`reconciliation_type`),
  KEY `idx_start_date` (`start_date`),
  KEY `idx_end_date` (`end_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对账批次表';

-- 对账明细表
CREATE TABLE IF NOT EXISTS `reconciliation_detail` (
  `detail_id` VARCHAR(36) NOT NULL COMMENT '明细ID',
  `batch_id` VARCHAR(36) NOT NULL COMMENT '批次ID',
  `entity_type` VARCHAR(50) NOT NULL COMMENT '实体类型',
  `internal_id` VARCHAR(36) COMMENT '内部ID',
  `external_id` VARCHAR(100) COMMENT '外部ID',
  `internal_value` JSON COMMENT '内部值',
  `external_value` JSON COMMENT '外部值',
  `status` VARCHAR(20) NOT NULL COMMENT '状态',
  `difference_reason` TEXT COMMENT '差异原因',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`detail_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_entity_type` (`entity_type`),
  KEY `idx_internal_id` (`internal_id`),
  KEY `idx_external_id` (`external_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对账明细表';

-- 对账汇总表
CREATE TABLE IF NOT EXISTS `reconciliation_summary` (
  `summary_id` VARCHAR(36) NOT NULL COMMENT '汇总ID',
  `batch_id` VARCHAR(36) NOT NULL COMMENT '批次ID',
  `summary_type` VARCHAR(50) NOT NULL COMMENT '汇总类型',
  `internal_total` DECIMAL(18,4) COMMENT '内部总计',
  `external_total` DECIMAL(18,4) COMMENT '外部总计',
  `difference` DECIMAL(18,4) COMMENT '差异金额',
  `status` VARCHAR(20) NOT NULL COMMENT '状态',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`summary_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_summary_type` (`summary_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对账汇总表';

-- 账实对账表
CREATE TABLE IF NOT EXISTS `reconciliation_physical` (
  `physical_id` VARCHAR(36) NOT NULL COMMENT '账实ID',
  `batch_id` VARCHAR(36) NOT NULL COMMENT '批次ID',
  `material_id` VARCHAR(36) NOT NULL COMMENT '物料ID',
  `factory_id` VARCHAR(36) NOT NULL COMMENT '工厂ID',
  `system_quantity` DECIMAL(18,4) NOT NULL COMMENT '系统数量',
  `actual_quantity` DECIMAL(18,4) NOT NULL COMMENT '实际数量',
  `difference` DECIMAL(18,4) COMMENT '差异数量',
  `status` VARCHAR(20) NOT NULL COMMENT '状态',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ext_json` JSON COMMENT '扩展字段',
  PRIMARY KEY (`physical_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_material_id` (`material_id`),
  KEY `idx_factory_id` (`factory_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账实对账表';

-- 初始化数据
INSERT INTO `material_category` (`category_id`, `category_code`, `category_name`, `created_by`) VALUES
('1', 'FABRIC', '面料', 'system'),
('2', 'ACCESSORY', '辅料', 'system');

INSERT INTO `mill` (`mill_id`, `mill_code`, `mill_name`, `created_by`) VALUES
('1', 'MILL001', '面料厂A', 'system'),
('2', 'MILL002', '面料厂B', 'system');

INSERT INTO `material` (`material_id`, `material_code`, `material_name`, `category_id`, `mill_id`, `unit`, `price`, `created_by`) VALUES
('1', 'MAT001', '纯棉面料', '1', '1', '米', 50.00, 'system'),
('2', 'MAT002', '涤纶面料', '1', '2', '米', 30.00, 'system');

INSERT INTO `factory` (`factory_id`, `factory_code`, `factory_name`, `capacity`, `created_by`) VALUES
('1', 'FAC001', '工厂A', 1000, 'system'),
('2', 'FAC002', '工厂B', 2000, 'system');

INSERT INTO `process` (`process_id`, `process_code`, `process_name`, `duration`, `created_by`) VALUES
('1', 'PROC001', '裁剪', 60, 'system'),
('2', 'PROC002', '缝制', 120, 'system'),
('3', 'PROC003', '熨烫', 30, 'system');
