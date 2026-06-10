# EC Demo

商品管理APIを題材に、バックエンド開発で重要なキャッシュ、非同期処理、検索、認証・認可、可用性設計を学習するためのプロジェクト。

---

# 技術スタック

## Backend

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Security

## Database / Middleware

* PostgreSQL
* Redis
* RabbitMQ
* OpenSearch
* Nginx

## Infrastructure

* Docker Compose
* Kubernetes（学習中）

## Monitoring

* Spring Boot Actuator
* Resilience4j

---

# アーキテクチャ

```text
Client
  ↓
Nginx
  ↓
Spring Boot API
  ├─ PostgreSQL
  ├─ Redis
  ├─ RabbitMQ
  └─ OpenSearch
```

詳細は `docs/architecture.md` を参照。

---

# 実装済み機能

## 商品管理

* 商品CRUD
* ページング
* ソート
* 検索

## キャッシュ

* Redis Cache
* Cache Aside Pattern
* ETag
* Cache-Control
* HTTP 304 Not Modified
* Nginx Cache

## 非同期処理

* RabbitMQ
* Outbox Pattern
* Retry
* Dead Letter Queue
* Idempotency

## 検索

* OpenSearch
* 商品検索API
* Reindex API

## 認証・認可

* Spring Security
* BCrypt
* JWT
* Refresh Token
* Role-Based Access Control

## 可用性

* Health Check
* Metrics
* Circuit Breaker
* Rate Limiter

## スケーリング

* Redis Distributed Lock
* Read Replica設計検討
* Kubernetes基礎学習

---

# 学習した設計パターン

* Cache Aside Pattern
* Outbox Pattern
* Idempotent Consumer
* Retry
* Dead Letter Queue
* Circuit Breaker
* Distributed Lock

---

# ドキュメント

| ファイル                     | 内容     |
| ------------------------ | ------ |
| docs/architecture.md     | システム構成 |
| docs/decisions.md        | ADR    |
| docs/development-plan.md | 開発計画   |
| docs/lessons-learned.md  | 学習記録   |

---

# 今後の発展項目

* OAuth2 / OIDC
* Kafka
* Prometheus / Grafana
* OpenSearch日本語検索
* Distributed Tracing
* Kubernetes本格導入
