# EC Demo API

Spring Bootを利用した商品管理APIです。

単純なCRUD実装に加え、パフォーマンス・可用性・データ整合性を考慮した設計を段階的に学習・実装することを目的としています。

---

# 技術スタック

## Backend

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate

## Database

* PostgreSQL
* Flyway

## Documentation

* Swagger / OpenAPI

## Development Environment

* Docker Compose
* Gradle

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

---

# API一覧

| Method | Path               | Description |
| ------ | ------------------ | ----------- |
| GET    | /api/products      | 商品一覧取得      |
| GET    | /api/products/{id} | 商品詳細取得      |
| POST   | /api/products      | 商品作成        |
| PUT    | /api/products/{id} | 商品更新        |
| DELETE | /api/products/{id} | 商品削除        |

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

---

# プロジェクト構成

```text
src/main/java
├── common
│   ├── config
│   ├── entity
│   └── exception
├── category
│   ├── entity
│   └── repository
└── product
    ├── controller
    ├── dto
    ├── entity
    ├── repository
    └── service
```

---

# 設計方針

* PostgreSQLを正本データとして扱う
* APIとDB構造を分離するためDTOを利用する
* Open Session In Viewを無効化する
* 一覧取得はDTO Projectionを利用する
* 詳細取得はFetch Joinを利用する
* DB変更はFlywayで管理する

---

# 今後の実装予定

## Phase2 パフォーマンス改善

* Redis Cache
* Cache Aside Pattern
* ETag
* Cache-Control
* Nginx Cache

## Phase3 非同期処理・検索

* RabbitMQ
* Outbox Pattern
* Idempotency
* Dead Letter Queue
* Elasticsearch

## Phase4 可用性・運用

* Retry
* Timeout
* Circuit Breaker
* Monitoring
* Read Replica
* Sharding

---

# ドキュメント

詳細な設計判断や開発計画については以下を参照してください。

* docs/development-plan.md
* docs/decisions.md
