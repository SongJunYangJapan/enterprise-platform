from fastapi import FastAPI

# Add future AI summary, RAG and Agent routers under app/routers/.
# This service does not participate in today's diagnosis request.
app = FastAPI(title="Platform AI Service")


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "UP"}
