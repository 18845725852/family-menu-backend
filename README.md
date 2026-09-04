# 家庭点菜小程序后端

这是一个独立的 Spring Boot 单体后端，面向家庭版微信点菜小程序的第一版核心流程：

> 查看可供应菜品 -> 家人提交订单 -> 查看历史订单

## 技术栈

- Java 8
- Spring Boot 2.7.18
- Maven
- Spring MVC + Bean Validation
- MySQL + Spring JDBC（默认）

## 启动

在本目录执行：

```bash
mvn spring-boot:run
```

项目已按 Java 8 编译验证通过。

## MySQL 配置

默认连接 `127.0.0.1:3306/family_menu`，用户名默认是 `root`，密码通过环境变量传入。首次使用先创建数据库：

```sql
CREATE DATABASE family_menu DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

再按本机账号启动：

```bash
export MYSQL_USERNAME=root
export MYSQL_PASSWORD='你的MySQL密码'
mvn spring-boot:run
```

应用启动时会自动执行 `schema.sql` 建表，并通过 `data.sql` 补充两道初始菜。也可以完整覆盖 JDBC 地址：

```bash
export MYSQL_URL='jdbc:mysql://127.0.0.1:3306/family_menu?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true'
```

如果 `orders` 表是在移除订单状态之前创建的，请在 `family_menu` 库中一次性执行 `migration-no-order-status.sql`，删除旧的 `status` 字段后再启动应用。

如果暂时没有可用 MySQL，可使用内存模式回归接口（数据重启后清空）：

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=memory
```

## 接口

启动后访问 `http://localhost:8081/` 可打开临时测试台，用于查看菜单、加入点菜篮、提交订单和查看历史订单。若服务在页面加入前已经启动，需要重启服务才能加载该页面。

### 查询可用菜品

```http
GET /api/dishes?category=家常菜
```

### 新增菜品（暂未做管理员鉴权）

```http
POST /api/dishes
Content-Type: application/json

{
  "name": "清炒西兰花",
  "category": "素菜",
  "sort": 3
}
```

### 编辑菜品（暂未做管理员鉴权）

```http
PUT /api/dishes/1
Content-Type: application/json

{
  "name": "番茄炒蛋（家常）",
  "category": "家常菜",
  "sort": 1
}
```

### 上下架菜品

```http
PATCH /api/dishes/1/availability
Content-Type: application/json

{"available": false}
```

### 删除菜品

```http
DELETE /api/dishes/1
```

### 提交点菜订单

```http
POST /api/orders
Content-Type: application/json

{
  "customerName": "妈妈",
  "items": [
    {"dishId": 1, "quantity": 1, "remark": "少油"},
    {"dishId": 2, "quantity": 1}
  ],
  "remark": "六点开饭"
}
```

### 查询订单

```http
GET /api/orders
GET /api/orders
```

### 查询订单详情

```http
GET /api/orders/1
```

### 删除订单

```http
DELETE /api/orders/1
```

## 下一步接入微信

1. 增加微信 `code2session` Gateway，以 `openid` 替换 `customerName` 的人工填写。
2. 增加家庭空间和成员表，再给“新增菜品”接口加管理员权限。
