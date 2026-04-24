FROM python:3.10-slim
WORKDIR /app
COPY . .
RUN pip install --no-cache-dir -r requirements.txt
# Cloud Run gives you a PORT environment variable, usually 8080
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8080"]