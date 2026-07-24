from fastapi import FastAPI

from app.routers import users_router

app = FastAPI(title="FinWiz API")

app.include_router(users_router)


@app.get("/")
async def root():
    return {"message": "FinWiz работает!"}


@app.get("/health")
async def health():
    return {"status": "ok"}
