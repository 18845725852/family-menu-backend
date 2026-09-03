-- 仅当 orders 表来自旧版本、仍存在 status 字段时执行一次。
DROP INDEX idx_orders_status_created ON orders;
ALTER TABLE orders DROP COLUMN status;
