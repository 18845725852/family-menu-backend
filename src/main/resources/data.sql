INSERT INTO restaurants(name, sort, enabled) VALUES
('鲁园外地锅鸡', 1, TRUE),
('牛new寿喜烧', 2, TRUE),
('西塔老太太烤肉', 3, TRUE),
('烤匠', 4, TRUE),
('马丫东北菜', 5, TRUE),
('海底捞火锅', 6, TRUE),
('鹊拾湘', 7, TRUE),
('一绪寿喜烧', 8, TRUE),
('添添潮牛', 9, TRUE),
('荆九爷爆炒', 10, TRUE)
ON DUPLICATE KEY UPDATE sort = VALUES(sort), enabled = VALUES(enabled);
