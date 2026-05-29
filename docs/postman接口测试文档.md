# handle-service Postman 接口测试文档

## 文件

- Postman Collection: `docs/handle-service.postman_collection.json`
- Postman Environment: `docs/handle-service.postman_environment.json`

## 运行前确认

1. 后端已启动，默认地址为 `http://localhost:3000`。
2. MySQL 中已经执行 `src/main/resources/db/schema.sql`，至少存在初始用户：
   - 用户账号：`user001`
   - 用户密码：`user123`
   - 管理员账号：`admin`
   - 管理员密码：`admin123`
3. 如果你的数据库账号密码不同，先确认 `src/main/resources/application.yml` 中的数据源配置。
4. 图片上传接口已预填项目内示例图片路径 `src/main/resources/pictures/user00120241205042539.jpg`；如果 Postman 导入后没有权限读取，请在 `form-data` 的 `picture` 字段手动重新选择图片。
5. `/uploadimage_path` 和 `/image/process` 会调用本机 `http://127.0.0.1:5000/predict`。如果图像识别服务没启动，这两个接口会失败，这是外部服务问题。
6. 本项目提供了一个本地模拟识别服务：`mock_predict_server.py`，启动后会返回 `class_indict = "9Z"`。

## 导入方式

1. 打开 Postman。
2. Import `docs/handle-service.postman_collection.json`。
3. Import `docs/handle-service.postman_environment.json`。
4. 右上角环境选择 `handle-service local`。
5. 先单独运行 `01 Auth / 用户登录`，确认自动写入 `token` 和 `userId`。
6. 再按文件夹顺序测试。

## 变量说明

| 变量 | 说明 |
|---|---|
| `baseUrl` | 后端地址，默认 `http://localhost:3000` |
| `token` | 用户登录后自动保存 |
| `adminToken` | 管理员登录后自动保存 |
| `userId` | 用户登录后自动保存 |
| `existingStratumId` | 数据库已有地层 ID，默认 `S2023001` |
| `existingImageId` | 数据库已有图片 ID，默认 `IMG2023001` |
| `existingLocalImageId` | 本地 pictures 目录已有图片名，不带 `.jpg` |
| `testUserAccount` | Collection 自动生成的测试用户账号 |
| `testUserNum` | Collection 自动生成的测试用户工号 |
| `testUserDbId` | 查询测试用户后自动保存的数据库主键 |
| `testStratumId` | Collection 自动生成的测试地层 ID |
| `testImageId` | Collection 自动生成的测试图片 ID |

## 推荐测试顺序

1. `01 Auth`
2. `02 Lookup`
3. `03 User`
4. `04 Stratum`
5. `05 Image`
6. `06 Log`
7. `99 Cleanup`

## 重点风险

- 不能保证“所有后端都没问题”。目前只确认编译通过，并补齐了部分 Mapper 缺失 SQL。仍需要按此 Postman 集合连库逐项验证。
- `99 Cleanup` 会删除测试数据，建议确认新增和更新成功后再执行。
- `/images/{image_id}` 返回纯 Base64 字符串，不是统一 `ApiResponse`。
- `/get/checkStratumIntegrity_notrigger` 的 `data.result` 可能是字符串或数组。
- 如果数据库里没有初始数据，请先执行建表和初始化 SQL，或者修改环境变量里的账号、地层、图片 ID。
- `/change/showImageInfo` 已按客户端页面扩展支持 `imageId`、`imageName`、`segType`、`segLen`、`segStart`、`segEnd`、`stratumId`、`uploaderNum`，返回项包含 `stratumName`。
- Collection 中请求数据已尽量按客户端页面字段给全：用户管理、岩柱管理、岩芯查询、图片元数据入库和日志接口都带完整示例字段。
- `/change/updateSubmit` 需要 `Authorization: Bearer <token>`，后端会校验当前登录用户是否为该图片的上传者。
