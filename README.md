# EC Demo API

Spring Bootを利用した商品管理APIです。

単純なCRUD実装に加え、パフォーマンス・可用性・データ整合性を考慮した設計を段階的に学習・実装することを目的としています。

---

# 技術スタック

## Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Cache
- Spring AMQP

## Database / Middleware

- PostgreSQL
- Redis
- RabbitMQ
- OpenSearch
- Nginx

## Migration / Documentation

- Flyway
- Swagger / OpenAPI

## Development

- Docker Compose
- Gradle

---

# 実装済み機能

## 商品管理

* 商品作成
* 商品一覧取得
* 商品詳細取得
* 商品更新
* 商品削除

## カテゴリ管理

* カテゴリテーブル管理
* 商品とカテゴリの関連付け

## API

* ページング
* ソート
* カテゴリ絞り込み
* キーワード検索

## 品質向上

* Request DTO
* Response DTO
* Validation
* Global Exception Handler
* Custom Exception

## DB設計

* Flywayによるマイグレーション管理
* Index作成
* 楽観ロック（@Version）

## JPA最適化

* DTO Projection
* Fetch Join
* Open Session In View無効化

## 共通化

* BaseEntity
* JPA Auditing

## キャッシュ

* Redis Cache
* Cache Aside Pattern
* ETag
* Cache-Control
* Nginx Cache

## 非同期処理

* RabbitMQ
* Producer / Consumer
* Retry
* Dead Letter Queue
* Idempotency
* Outbox Pattern

## 検索

* OpenSearch
* 商品検索API
* 商品更新時の非同期Index更新
* 商品削除時の非同期Index削除
* Reindex API

## 認証・認可

* Spring Security
* BCryptによるパスワードハッシュ化
* Login API
* JWT Access Token
* Refresh Token
* Logout API
* Current User API
* Role-Based Access Control
* 401 / 403 Error Handling

### 可用性・運用

* Spring Boot Actuator
* Health Check
* Metrics
* OpenSearch Custom HealthIndicator
* OpenSearch Client Timeout
* Resilience4j Circuit Breaker
* Fallback

---

# API一覧

## 認証

| Method | Path | 説明 | 認可 |
|---|---|---|---|
| POST | /api/auth/login | ログインしてAccess Token / Refresh Tokenを発行 | 公開 |
| POST | /api/auth/refresh | Refresh TokenでAccess Tokenを再発行 | 公開 |
| POST | /api/auth/logout | Refresh Tokenを削除してログアウト | 公開 |

## ユーザー

| Method | Path | 説明 | 認可 |
|---|---|---|---|
| GET | /api/users/me | ログイン中ユーザー情報を取得 | USER / ADMIN |

## 商品

| Method | Path | 説明 | 認可 |
|---|---|---|---|
| GET | /api/products | 商品一覧取得 | 公開 |
| GET | /api/products/{id} | 商品詳細取得 | 公開 |
| POST | /api/products | 商品作成 | ADMIN |
| PUT | /api/products/{id} | 商品更新 | ADMIN |
| DELETE | /api/products/{id} | 商品削除 | ADMIN |
| GET | /api/products/search?keyword=xxx | 商品検索 | 公開 |

## 管理

| Method | Path | 説明 | 認可 |
|---|---|---|---|
| POST | /api/admin/search/reindex | OpenSearchのIndexを再構築 | ADMIN |

## 運用

| Method | Path | 説明 | 認可 |
|---|---|---|---|
| GET | /actuator/health | Health Check | 公開または制限 |
| GET | /actuator/metrics | Metrics一覧 | 公開または制限 |
| GET | /actuator/circuitbreakers | Circuit Breaker状態確認 | 公開または制限 |

---

# ローカル起動

## PostgreSQL起動

```bash
docker compose up -d
```

## アプリケーション起動

```bash
./gradlew bootRun
```

---

# APIドキュメント

Swagger UI

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI

```text
http://localhost:8080/v3/api-docs
```

# 設計方針

* PostgreSQLを正本データとして扱う
* APIとDB構造を分離するためDTOを利用する
* Open Session In Viewを無効化する
* 一覧取得はDTO Projectionを利用する
* 詳細取得はFetch Joinを利用する
* DB変更はFlywayで管理する

---

# 今後の実装予定

## Optional

* Kafka比較
* Kubernetes manifest

---

# ドキュメント

詳細な設計判断や開発計画については以下を参照してください。

* docs/development-plan.md
* docs/decisions.md
