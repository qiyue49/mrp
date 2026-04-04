-- 测试数据
-- 物料分类
INSERT INTO `material_category` (`category_id`, `category_code`, `category_name`, `created_by`) VALUES
('1', 'FABRIC', 'Fabric', 'test'),
('2', 'ACCESSORY', 'Accessory', 'test');

-- 面料厂
INSERT INTO `mill` (`mill_id`, `mill_code`, `mill_name`, `created_by`) VALUES
('1', 'MILL001', 'Mill A', 'test'),
('2', 'MILL002', 'Mill B', 'test');

-- 物料
INSERT INTO `material` (`material_id`, `material_code`, `material_name`, `category_id`, `mill_id`, `unit`, `price`, `created_by`) VALUES
('1', 'MAT001', 'Cotton Fabric', '1', '1', 'meter', 50.00, 'test'),
('2', 'MAT002', 'Polyester Fabric', '1', '2', 'meter', 30.00, 'test'),
('3', 'MAT003', 'Zipper', '2', '1', 'piece', 5.00, 'test'),
('4', 'MAT004', 'Button', '2', '2', 'piece', 0.50, 'test');

-- 工厂
INSERT INTO `factory` (`factory_id`, `factory_code`, `factory_name`, `capacity`, `created_by`) VALUES
('1', 'FAC001', 'Factory A', 1000, 'test'),
('2', 'FAC002', 'Factory B', 2000, 'test'),
('3', 'FAC003', 'Factory C', 500, 'test');

-- 工艺
INSERT INTO `process` (`process_id`, `process_code`, `process_name`, `duration`, `created_by`) VALUES
('1', 'PROC001', 'Cutting', 60, 'test'),
('2', 'PROC002', 'Sewing', 120, 'test'),
('3', 'PROC003', 'Ironing', 30, 'test');

-- 设计
INSERT INTO `design` (`design_id`, `design_code`, `creator_id`, `created_by`) VALUES
('1', 'DESIGN001', 'CREATOR001', 'test'),
('2', 'DESIGN002', 'CREATOR002', 'test'),
('3', 'DESIGN003', 'CREATOR003', 'test');

-- 订单
INSERT INTO `orders` (`order_id`, `order_code`, `customer_id`, `design_id`, `quantity`, `delivery_date`, `status`, `created_by`) VALUES
('1', 'ORD001', 'CUST001', '1', 100, '2026-05-01', 'CREATED', 'test'),
('2', 'ORD002', 'CUST002', '2', 500, '2026-04-15', 'CREATED', 'test'),
('3', 'ORD003', 'CUST003', '3', 50, '2026-04-10', 'CREATED', 'test');
