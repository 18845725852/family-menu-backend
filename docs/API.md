# 接口说明

[返回项目说明](../README.md) · [部署与维护](DEPLOYMENT.md)

本文路径以服务地址为前缀，例如本地 `http://localhost:8081`。

## 业务规则

家庭默认使用系统菜谱。首次开启自定义菜谱时复制可用的默认菜品，后续重新开启保留已有内容。家庭菜谱只能使用系统已有的启用分类；被历史订单引用的菜品不能删除。

家庭菜谱和候选餐厅在家庭内共享。自定义餐厅支持 3～16 家，名称去除首尾空格后不能重复，每个不超过 20 个字。抽卡结果保存到家庭记录；家庭抽卡概览返回最近 5 条记录。

创建者可以改名、移除其他成员及删除家庭；其他成员可退出自己所在的家庭。创建者不能直接退出，删除家庭会清理其订单、自定义菜品、邀请码、抽卡记录及候选项。邀请码为 6 位，正式数据库模式有效期 7 天。

订单目前是选菜记录，没有价格、支付、制作状态流转或下单消息提醒。

## 接口约定

接口通常返回 `{ success, message, data }`。业务和参数错误通常返回 HTTP 400，错误信息位于 `data.code`、`data.message`；缺少有效登录会话的受保护接口返回 HTTP 401，可能没有响应体。

需要登录的请求携带：

```http
Authorization: Bearer <登录返回的 token>
```

### 账号和家庭

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/wechat-login` | 登录，请求体含 `code`，可带 `nickname` |
| POST | `/api/auth/logout` | 注销当前会话 |
| PUT | `/api/me/profile` | 更新 `nickname`，可带 `avatarUrl` |
| GET / POST | `/api/families` | 查询自己的家庭 / 创建家庭 |
| GET / PUT / DELETE | `/api/families/{familyId}` | 家庭详情 / 创建者改名 / 创建者删除 |
| GET | `/api/families/{familyId}/members` | 家庭成员列表 |
| PATCH | `/api/families/{familyId}/members/{memberId}/role` | 创建者调整非拥有者成员角色，支持 `ADMIN`、`MEMBER` |
| DELETE | `/api/families/{familyId}/members/{memberId}` | 创建者移除成员，或成员自行退出 |
| POST | `/api/families/{familyId}/invite-code` | 家庭成员生成邀请码 |
| GET | `/api/family-invitations/preview?inviteCode=XXXXXX` | 预览邀请的家庭 |
| POST | `/api/family-invitations/join` | 加入家庭，请求体为 `{"inviteCode":"XXXXXX"}` |

除登录接口外，上述接口均要求登录。

### 家庭菜谱与订单

以下接口均要求登录并属于指定家庭：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/families/{familyId}/menu` | 返回 `customMenu` 和可用菜品 |
| PUT | `/api/families/{familyId}/menu` | 请求体 `{"customMenu":true}`，切换菜谱模式 |
| POST | `/api/families/{familyId}/menu/dishes` | 新增家庭菜品，须已开启自定义菜谱 |
| PUT / DELETE | `/api/families/{familyId}/menu/dishes/{dishId}` | 编辑 / 删除家庭菜品 |
| POST | `/api/families/{familyId}/menu/dishes/batch-delete` | 请求体含 `ids` 数组，返回删除数量及受历史订单阻止的菜品 |
| GET / POST | `/api/families/{familyId}/orders` | 查询家庭订单 / 下单 |
| GET / DELETE | `/api/families/{familyId}/orders/{orderId}` | 订单详情 / 删除订单 |

新增或编辑家庭菜品示例（分类须已存在且启用）：

```json
{
  "name": "清炒西兰花",
  "category": "素菜",
  "description": "清淡家常菜",
  "recipe": "1. 西兰花洗净焯水\n2. 下锅翻炒并调味",
  "imageUrl": "/api/files/dishes/families/1/<上传返回的文件名>",
  "sort": 3
}
```

提交订单示例：

```json
{
  "items": [
    { "dishId": 1, "quantity": 1, "remark": "少油" },
    { "dishId": 2, "quantity": 2 }
  ],
  "remark": "六点开饭"
}
```

服务端校验菜品可用性及其是否属于当前使用的默认或家庭菜谱。

### 餐厅抽卡

以下家庭接口要求登录及家庭成员身份：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/families/{familyId}/wheel` | 候选餐厅、是否自定义及最近 5 条记录 |
| PUT | `/api/families/{familyId}/wheel/restaurants` | 整体替换候选项，请求体 `{"items":[{"name":"餐厅一"},{"name":"餐厅二"},{"name":"餐厅三"}]}` |
| GET | `/api/families/{familyId}/wheel/spins` | 全部家庭抽卡记录 |
| POST | `/api/families/{familyId}/wheel/spins` | 保存结果，请求体 `{"name":"餐厅一"}` |

另外保留 `/api/me/wheel-restaurants` 的 GET / PUT 个人候选项接口；当前小程序主要使用家庭共享接口。

### 公共菜品、分类和餐厅

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET / POST | `/api/dishes` | 查询默认可用菜品 / 新增默认菜品；查询支持 `category` |
| PUT / DELETE | `/api/dishes/{id}` | 编辑 / 删除菜品 |
| PATCH | `/api/dishes/{id}/availability` | 请求体 `{"available":false}`，调整供应状态 |
| GET / POST | `/api/dish-categories` | 查询启用分类 / 新增分类；`all=true` 可查全部 |
| PUT / DELETE | `/api/dish-categories/{id}` | 编辑 / 删除分类 |
| GET / POST | `/api/restaurants` | 查询启用餐厅 / 新增默认餐厅；`all=true` 可查全部 |
| PUT / DELETE | `/api/restaurants/{id}` | 编辑 / 删除默认餐厅 |
| GET | `/api/restaurants/spin` | 随机返回一家启用的默认餐厅，不保存家庭记录 |

公共资源的维护操作应通过受控的管理流程使用。

## 图片上传与访问

图片不放进部署包。上传接口为 `POST /api/uploads/image`，使用 `multipart/form-data`，字段如下：

| 字段 | 含义 |
| --- | --- |
| `file` | 必填图片，最大 10MB；上传后最长边缩至不超过 640 像素，PNG 保留透明度，其他可解码图片转 JPEG |
| `category` | 默认菜品可省略或传 `dishes` / `default`；头像传 `avatars`；家庭菜品传 `family` |
| `familyId` | `category=family` 时必填正数家庭 ID |

返回的 `data` 是相对访问地址，前端按后端服务地址补全：

| 类型 | 磁盘目录（相对图片根目录） | 访问地址 |
| --- | --- | --- |
| 默认菜品 | `dishes/default/` | `/api/files/dishes/default/{文件名}` |
| 家庭菜品 | `dishes/families/{familyId}/` | `/api/files/dishes/families/{familyId}/{文件名}` |
| 头像 | `avatars/` | `/api/files/avatars/{文件名}` |

默认根目录为当前用户家目录下的 `.family-menu/files`。生产环境通过 `FILE_STORAGE_DIR` 指定项目外的持久目录，发布 JAR 时保留并备份它。迁移到 `legacy/` 的旧图片继续通过 `/uploads/{文件名}` 访问；启动时不会从项目目录复制旧图。
