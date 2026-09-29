from fastapi import FastAPI

# 将来的 AI 总结、RAG（知识库检索增强）和 Agent 功能，都放在 app/routers/ 目录下。
# 当前这个服务不参与现有的诊断请求流程，只提供 /health 健康检查。
app = FastAPI(title="Platform AI Service")


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "UP"}
