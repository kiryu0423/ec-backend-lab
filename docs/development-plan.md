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

## Phase6 アーキテクチャ・スケーリング

### 実装・検討内容

* RabbitMQ / Kafka比較
* Redis Distributed Lock
* Rate Limiter
* Read Replica
* Kubernetes概要

### 学習ポイント

* メッセージング基盤の使い分け
* 複数インスタンス起動時の排他制御
* API保護
* 読み取り負荷分散
* コンテナオーケストレーション

### Read Replica

読み取り負荷が増えた場合、PostgreSQLのRead Replicaを検討する。

* GET系APIはReplica参照候補
* 更新系APIはPrimary参照
* 在庫・決済など強い整合性が必要な処理はPrimary参照
* Replication Lagによる一時的な不整合を許容できるか検討する

---

## 発展項目

## Phase7 認証・外部連携

状態: 発展項目

### 実装・検討内容

- OAuth2
- OIDC
- Google Login
- 外部IdP連携

### 学習ポイント

- 認可コードフロー
- ID Token
- Access Token
- Resource Server
- Identity Provider

---

## Phase8 大規模データ設計

状態: 発展項目

### 実装・検討内容

- Sharding
- Hot Shard
- Read / Write分離
- データ分割設計

### 学習ポイント

- Shard Key設計
- Replication Lag
- 分散トランザクション
- データ整合性

---

## Phase9 検索高度化

状態: 発展項目

### 実装・検討内容

- 日本語Analyzer
- Synonym
- Relevance Tuning
- Suggest / Autocomplete

### 学習ポイント

- Analyzer
- Tokenizer
- 検索スコア
- 表記ゆれ対応

---

## Phase10 運用・監視強化

状態: 発展項目

### 実装・検討内容

- Prometheus
- Grafana
- 構造化ログ
- Trace
- Alert設計

### 学習ポイント

- Metrics収集
- Dashboard設計
- 分散トレーシング
- 障害検知
