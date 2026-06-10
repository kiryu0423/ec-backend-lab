# 開発計画

## Phase1 商品管理API

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

### 実装内容

* Spring Security
* usersテーブル
* BCrypt
* Login API
* JWT Access Token
* Refresh Token
* Refresh API
* Logout API
* Current User API
* USER / ADMIN Role
* Role-Based Access Control
* 401 / 403 Error Handling

### 学習ポイント

* Stateless認証
* JWT署名検証
* Security Filter
* SecurityContext
* AuthenticationEntryPoint
* AccessDeniedHandler
* Role-Based Access Control

---

## Phase5 可用性・運用

状態: 完了

### 実装内容

* Spring Boot Actuator
* Health Check
* Metrics
* OpenSearch Custom HealthIndicator
* OpenSearch Client Timeout
* Resilience4j Circuit Breaker
* Fallback処理
* Circuit Breaker状態確認

### 学習ポイント

* Health Check
* Metrics
* Graceful Degradation
* Timeout
* Circuit Breaker
* CLOSED / OPEN / HALF_OPEN

---

## Optional

### 分散システム

* Kafka比較
* Read Replica
* Sharding

### インフラ

* Kubernetes
* Deployment
* Service
* ConfigMap

### 検索

* 日本語Analyzer
* Synonym
* Relevance Tuning

### 認証・認可

* OAuth2 / OIDC
