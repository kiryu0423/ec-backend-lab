# 開発計画

## Phase1 商品管理API

状態: 完了

### 実装内容

* 商品CRUD
* カテゴリ管理
* Validation
* Global Exception Handler
* Custom Exception
* Paging
* Sorting
* DTO Projection
* Fetch Join
* Open Session In View無効化
* Swagger / OpenAPI
* Flyway
* 楽観Lock
* Auditing
* BaseEntity

---

## Phase2 パフォーマンス改善

状態: 完了

### 実装内容

* Redis Cache
* Cache Aside Pattern
* Cache Evict
* ETag
* Cache-Control
* HTTP 304 Not Modified
* Nginx Cache

### 学習ポイント

* キャッシュ階層

  * Browser Cache
  * Nginx Cache
  * Redis Cache
  * DB Cache
* キャッシュ整合性
* HTTPキャッシュ

---

## Phase3 非同期処理・検索

状態: 完了

### 実装内容

* RabbitMQ
* Producer
* Consumer
* Retry
* Dead Letter Queue
* Idempotency
* Outbox Pattern
* OpenSearch
* 商品検索API
* 非同期Index更新
* 非同期Index削除
* Reindex API

### 学習ポイント

* At Least Once Delivery
* Event Driven Architecture
* Eventually Consistent
* Transaction Boundary
* Distributed System Failure
* Search Engine Architecture

---

## Phase4 認証・認可

状態: 未着手

### 実装予定

* Spring Security
* Login API
* JWT
* Access Token
* Role-Based Access Control
* USER / ADMIN権限

---

## Phase5 可用性・運用

状態: 未着手

### 実装予定

* Timeout
* Retry Policy
* Circuit Breaker
* Monitoring
* Read Replica
* Sharding

---

## Optional

### メッセージング

* Kafka比較

### インフラ

* Kubernetes
* Deployment
* Service
* ConfigMap

### 検索

* 日本語Analyzer
* Synonym
* Relevance Tuning
