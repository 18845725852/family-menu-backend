# 部署与维护

[返回项目说明](../README.md) · [接口说明](API.md)

本文文件路径以本后端仓库根目录为准。

## 环境配置

数据库、微信登录及图片目录的基本配置见项目 README。需要覆盖 JDBC 地址时，在 PowerShell 中设置：

```powershell
$env:MYSQL_URL = 'jdbc:mysql://127.0.0.1:3306/family_menu?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true'
```

凭据通过环境变量或本地外部配置提供，不写入仓库。

## 数据初始化与升级

- 默认启动会执行 `src/main/resources/schema.sql` 和 `data.sql`。`schema.sql` 使用 `CREATE TABLE IF NOT EXISTS`；当前 `data.sql` 初始化默认餐厅，并按名称更新已有餐厅的排序及启用状态，**不会初始化默认菜品或分类**。
- 新建数据库后，需自行准备默认分类和菜品，或在明确允许重置的开发库中使用重置脚本。
- 自动兼容逻辑会补充家庭菜谱的 `dishes.family_id`、索引和 `families.custom_menu`，并检查家庭抽卡表；这不等于完成所有旧库升级。
- 旧库升级前备份数据库和图片目录，核对实际表结构后选择 `src/main/resources/migration-*.sql`。不要将所有脚本重复或无差别执行。
- 登录会话及订单关联升级参考 `migration-user-session-and-order-family.sql`、`migration-order-family.sql`；邀请码参考 `migration-family-invitation.sql`；家庭抽卡参考 `migration-family-wheel.sql`。菜品介绍、做法及旧订单字段的变更分别有对应迁移脚本，需先检查脚本内容和现有字段。
- `reset-and-seed-menu.sql` 会清空现有订单、菜品和分类，仅用于明确需要重置数据的场景，**不能作为保留生产数据的升级脚本**；应用启动不会自动执行它。

## 图片目录

图片保存在 `${user.home}/.family-menu/files`，可通过 `FILE_STORAGE_DIR` 改为项目外的持久目录。发布 JAR 时须保留并备份此目录；开发内存模式也会将图片写入磁盘。目录划分及访问地址见[图片上传与访问](API.md#图片上传与访问)。

## 发布核对

使用正式 HTTPS 地址、持久化数据库及对应的微信配置，核对接口权限，并完成登录、图片上传及家庭数据隔离验收。
