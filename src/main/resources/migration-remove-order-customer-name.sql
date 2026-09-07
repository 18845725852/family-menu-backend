-- Stop the old application, execute this script once, then start the new application.
-- Historical orders keep creator_user_id, so changing users.nickname updates all order displays.
ALTER TABLE orders DROP COLUMN customer_name;
