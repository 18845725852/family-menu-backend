# 家庭菜单后端

为微信小程序提供登录、家庭管理、共享菜谱、点菜订单、餐厅抽卡和图片存储服务。页面说明见[前端仓库](https://github.com/18845725852/family-menu-frontend)。前后端独立获取，无需放在同级目录。

## 功能概览

| 模块 | 功能 |
| --- | --- |
| 账号与家庭 | 微信登录、个人资料、创建家庭、邀请码加入、成员管理及多家庭切换 |
| 菜谱 | 默认及家庭自定义菜谱、图片和做法、单个及批量删除 |
| 订单 | 提交家庭订单、查询记录和详情、删除订单 |
| 抽卡 | 默认及家庭候选餐厅、保存和查询抽卡记录 |
| 图片 | 上传压缩、分类存储及图片访问 |

菜谱、候选餐厅和订单在家庭内共享。被历史订单引用的菜品不能删除。删除家庭会清理其订单、自定义菜品、邀请码和抽卡数据。订单用于记录选菜，当前不含支付和制作状态流转。

## 本地启动

环境：Java 8、Maven、MySQL；后端使用 Spring Boot 2.7.18 和 Spring JDBC。以下 PowerShell 命令在本仓库根目录执行，默认端口为 8081。

### 开发内存模式

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=memory"
```

无需 MySQL，使用固定开发用户。业务数据重启后清空，图片仍写入磁盘；仅用于开发，不能验证真实微信登录和多账号隔离。

### MySQL 模式

先创建数据库，并为数据库账号授予相应权限：

```sql
CREATE DATABASE family_menu DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

配置环境变量并启动：

```powershell
$env:MYSQL_USERNAME = 'family_menu'
$env:MYSQL_PASSWORD = '<数据库密码>'
$env:WECHAT_APPID = '<微信小程序 AppID>'
$env:WECHAT_SECRET = '<微信小程序密钥>'
mvn spring-boot:run
```

| 环境变量 | 用途 |
| --- | --- |
| `MYSQL_URL` | JDBC 地址，默认连接 `127.0.0.1:3306/family_menu` |
| `MYSQL_USERNAME` / `MYSQL_PASSWORD` | 数据库账号及密码 |
| `WECHAT_APPID` / `WECHAT_SECRET` | 与前端对应的小程序 AppID 及密钥 |
| `FILE_STORAGE_DIR` | 图片根目录，默认为当前用户家目录下的 `.family-menu/files` |

敏感配置通过环境变量提供；本地也可使用已忽略的 `config/application.yml`。从本仓库根目录启动时会读取该外部配置；部署时通过环境变量或部署目录下的外部配置提供凭据。

## 数据与部署

- 启动会执行 `schema.sql` 和 `data.sql`，当前只初始化默认餐厅；新库需另行准备默认分类和菜品。
- 升级前备份数据库和图片目录；发布 JAR 时保留图片目录。
- **`reset-and-seed-menu.sql` 会清空订单、菜品和分类，不能用于保留生产数据的升级。**
- 正式部署需使用真实微信登录配置、HTTPS 和持久化数据库，并核对接口权限。详细配置及迁移说明见[部署与维护](docs/DEPLOYMENT.md)。

## 测试与打包

```powershell
mvn test
mvn package
```

现有测试覆盖家庭管理和菜谱初始化，使用内存仓库；真实数据库及微信登录需另行联调。

## 详细文档

- [接口与业务规则](docs/API.md)
- [部署与维护](docs/DEPLOYMENT.md)
