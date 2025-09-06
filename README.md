![Rate Limiter](docs/assets/rate-limiter.png)

## 🧠 What Is a Rate Limiter?
- A rate limiter controls how many requests a user or client can make within a given time window.
- It protects downstream services from overload/abuse, smooths traffic spikes, and helps manage costs for third‑party APIs.
- This project uses a token bucket strategy backed by Redis, with limits configured via S3 and refreshed on a schedule.

## 🚀 How To Run
- Requirements: `Docker`/`Docker Compose`, `JDK 21`, and either `Terraform` (or use `terraform/terraform.exe` on Windows).
- Start dependencies: `docker compose up -d`
- Create S3 bucket in LocalStack:
  - Windows: `cd terraform && .\terraform.exe init && .\terraform.exe apply -auto-approve`
  - macOS/Linux: `cd terraform && terraform init && terraform apply -auto-approve`
- Upload limits file to S3 (choose one):
  - With AWS CLI: `aws --endpoint-url http://localhost:4566 s3 cp limits.yml s3://ratelimiter-configs/limits.yml`
  - Or run helper `UploadRateLimitConfigFile` (from an IDE) at `modules/vendors/spring/.../UploadRateLimitConfigFile.java`.
- Run the app: on Windows `gradlew.bat :spring:bootRun`; on macOS/Linux `./gradlew :spring:bootRun`

## 🧪 Try It
- Endpoint: send requests to `http://localhost:8080/api/{resource}` with header `USER_ID` set. Example config in `limits.yml` limits `POST /api/posts` to `2` per minute per user.
- Example calls:
  - `curl -i -X POST http://localhost:8080/api/posts -H "USER_ID: 123"`
  - Repeat up to 2 times in a minute → `200 OK` with headers `X-RateLimit-Requests-Remaining` and `X-RateLimit-Requests-Limit`.
  - Further requests within the minute → `429 Too Many Requests` with `X-RateLimit-Retry-After-Seconds`.

## 🏗️ How It Works
- Token Bucket: consumes one token per request; refills each unit (second/minute/hour/day) using JobRunr.
- Config Source: `limits.yml` stored in S3 (via LocalStack); worker refreshes from S3 every second into Redis.
- Keys: limits are applied per action/resource pair (e.g., `create:posts`) and by identifier (e.g., user ID) or IP.

## 🧩 Configuration Tips
- Edit `limits.yml` to add more resources/actions. Supported units: `second`, `minute`, `hour`, `day`. Supported selectors: `identifier`, `ip`.
- App port defaults to `8080`. Redis runs at `localhost:6379`. LocalStack S3 at `http://localhost:4566`.
