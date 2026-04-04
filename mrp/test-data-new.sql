-- 测试数据
-- 物料分类
INSERT INTO `material_category` (`category_id`, `category_code`, `category_name`, `created_by`) VALUES
('101', 'FABRIC', 'Fabric', 'test'),
('102', 'ACCESSORY', 'Accessory', 'test')
ON DUPLICATE KEY UPDATE created_by = 'test';

-- 面料厂
INSERT INTO `mill` (`mill_id`, `mill_code`, `mill_name`, `created_by`) VALUES
('101', 'MILL001', 'Mill A', 'test'),
('102', 'MILL002', 'Mill B', 'test')
ON DUPLICATE KEY UPDATE created_by = 'test';

-- 物料
INSERT INTO `material` (`material_id`, `material_code`, `material_name`, `category_id`, `mill_id`, `unit`, `price`, `created_by`) VALUES
('101', 'MAT001', 'Cotton Fabric', '101', '101', 'meter', 50.00, 'test'),
('102', 'MAT002', 'Polyester Fabric', '101', '102', 'meter', 30.00, 'test'),
('103', 'MAT003', 'Zipper', '102', '101', 'piece', 5.00, 'test'),
('104', 'MAT004', 'Button', '102', '102', 'piece', 0.50, 'test')
ON DUPLICATE KEY UPDATE created_by = 'test';

-- 工厂
INSERT INTO `factory` (`factory_id`, `factory_code`, `factory_name`, `capacity`, `created_by`) VALUES
('101', 'FAC001', 'Factory A', 1000, 'test'),
('102', 'FAC002', 'Factory B', 2000, 'test'),
('103', 'FAC003', 'Factory C', 500, 'test')
ON DUPLICATE KEY UPDATE created_by = 'test';

-- 工艺
INSERT INTO `process` (`process_id`, `process_code`, `process_name`, `duration`, `created_by`) VALUES
('101', 'PROC001', 'Cutting', 60, 'test'),
('102', 'PROC002', 'Sewing', 120, 'test'),
('103', 'PROC003', 'Ironing', 30, 'test')
ON DUPLICATE KEY UPDATE created_by = 'test';

-- 设计
INSERT INTO `design` (`design_id`, `design_code`, `creator_id`, `created_by`) VALUES
('101', 'DESIGN001', 'CREATOR001', 'test'),
('102', 'DESIGN002', 'CREATOR002', 'test'),
('103', 'DESIGN003', 'CREATOR003', 'test')
ON DUPLICATE KEY UPDATE created_by = 'test';

-- 订单
INSERT INTO `orders` (`order_id`, `order_code`, `customer_id`, `design_id`, `quantity`, `delivery_date`, `status`, `created_by`) VALUES
('101', 'ORD001', 'CUST001', '101', 100, '2026-05-01', 'CREATED', 'test'),
('102', 'ORD002', 'CUST002', '102', 500, '2026-04-15', 'CREATED', 'test'),
('103', 'ORD003', 'CUST003', '103', 50, '2026-04-10', 'CREATED', 'test')
ON DUPLICATE KEY UPDATE created_by = 'test';
