-- 仅当 dishes 表来自旧版本、仍存在 description 字段时执行一次。
ALTER TABLE dishes DROP COLUMN description;
