-- Execute once after migration-user-session-and-order-family.sql.
-- Existing orders are assigned to the first existing family owner.
ALTER TABLE orders ADD COLUMN family_id BIGINT NULL AFTER id;
ALTER TABLE orders ADD COLUMN creator_user_id BIGINT NULL AFTER family_id;
UPDATE orders SET family_id = (SELECT id FROM families ORDER BY id LIMIT 1) WHERE family_id IS NULL;
UPDATE orders SET creator_user_id = (SELECT owner_user_id FROM families WHERE families.id = orders.family_id) WHERE creator_user_id IS NULL;
ALTER TABLE orders MODIFY family_id BIGINT NOT NULL;
ALTER TABLE orders MODIFY creator_user_id BIGINT NOT NULL;
ALTER TABLE orders ADD KEY idx_orders_family_id (family_id);
ALTER TABLE orders ADD CONSTRAINT fk_orders_family FOREIGN KEY (family_id) REFERENCES families (id);
ALTER TABLE orders ADD CONSTRAINT fk_orders_creator FOREIGN KEY (creator_user_id) REFERENCES users (id);
