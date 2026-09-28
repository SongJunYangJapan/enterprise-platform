# 企业统一平台工程骨架

本阶段只有一个 Spring Boot API、一个 Vue 页面、两张诊断表，以及独立的 FastAPI 健康检查。BOSS、LibreNMS、Dify、设备命令均留有 Adapter 边界，尚未接入真实系统。不要在生产环境启用 `dev` profile。

## 目录与后续修改位置

| 需要做的事 | 修改位置 |
|---|---|
| 新增诊断流程、状态流转、步骤记录 | `platform-api/src/main/java/com/company/platform/diagnosis/DiagnosisService.java` |
| 修改诊断 API 和输入校验 | `diagnosis/DiagnosisController.java`；同步修改 `web/src/api.ts` |
| 调整表结构 | 新增 `platform-api/src/main/resources/db/migration/V2__*.sql`；同步修改诊断行对象和 Mapper |
| 接入 BOSS / LibreNMS / 一期 Dify | `integration/boss`、`integration/librenms`、`integration/dify`；用 Spring `RestClient` 实现 Adapter，并在业务层调用 |
| 接入设备命令 | `device/DeviceCommandAdapter.java`；先落实命令模板、权限和审计要求 |
| 接入公司 SSO | `security/SecurityConfig.java`；配置 `SSO_ISSUER_URI` 并映射 JWT 权限；关闭 `dev` profile |
| 新增前端页面 | `web/src/`；通用请求配置在 `web/src/api.ts` |
| 新增 AI 总结、RAG 或 Agent | `ai-service/app/routers/`；先定义独立 API 契约 |

`caseId` 是本次诊断的追踪号，`circuitId` 是线路号。二者同时写入 `diagnosis_run` 和 `diagnosis_step`。真实集成时，所有 Adapter 请求和日志都要带上这两个值。

## 本地运行

要求 Java 21、Maven 3.6.3+、Node.js、pnpm、Python 3.12 和 Docker Compose。默认数据库密码仅供本地开发，部署时用环境变量覆盖。

```powershell
cd enterprise-platform/deploy
docker compose up -d mysql

cd ../platform-api
$env:SPRING_PROFILES_ACTIVE='dev'
mvn spring-boot:run

cd ../web
pnpm install
pnpm dev

cd ../ai-service
python -m venv .venv
.venv/Scripts/pip install -r requirements.txt
.venv/Scripts/uvicorn app.main:app --host 127.0.0.1 --port 8000
```

打开 `http://127.0.0.1:5173`。输入 `circuitId`，点击“发起诊断”，页面应显示记录和 SW/PE 两个 `PENDING` 步骤。刷新后页面按 URL 中的 `circuitId` 重新查询历史。

## 验证命令

```powershell
Invoke-RestMethod http://127.0.0.1:8080/actuator/health
Invoke-RestMethod http://127.0.0.1:8080/api/me
Invoke-RestMethod -Method Post http://127.0.0.1:8080/api/diagnoses -ContentType application/json -Body '{"circuitId":"TEST-001"}'
Invoke-RestMethod 'http://127.0.0.1:8080/api/diagnoses?circuitId=TEST-001'
Invoke-RestMethod http://127.0.0.1:8000/health
```

数据库可用 `docker compose exec mysql mysql -uplatform -pplatform_dev enterprise_platform -e "SELECT * FROM diagnosis_run; SELECT * FROM diagnosis_step;"` 检查。

## 安全边界

`dev` profile 用固定的 `dev.user` 模拟登录，不能用于共享或生产环境。其他 profile 使用 OAuth2 Resource Server 校验 JWT，须先设置真实的 `SSO_ISSUER_URI`。当前页面没有实现公司登录跳转；接入 SSO 后由公司门户提供令牌，再在 `web/src/api.ts` 增加 Authorization 请求头。
