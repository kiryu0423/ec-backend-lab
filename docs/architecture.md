# アーキテクチャ概要

本プロジェクトは、商品管理APIを題材に、バックエンド開発で重要となるキャッシュ、非同期処理、検索、認証・認可、可用性設計を段階的に実装する。

---

# 全体構成

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

## 各コンポーネントの役割

| コンポーネント     | 役割                           |
| ----------- | ---------------------------- |
| Nginx       | Reverse Proxy / HTTP Cache   |
| Spring Boot | API本体 / 業務ロジック               |
| PostgreSQL  | 正本データ                        |
| Redis       | 商品詳細キャッシュ / Distributed Lock |
| RabbitMQ    | 非同期イベント配送                    |
| OpenSearch  | 商品検索用Index                   |
| Flyway      | DB Migration                 |
| Actuator    | Health Check / Metrics       |

---

# データの正本

PostgreSQLを正本データとして扱う。

```text
PostgreSQL
→ Source of Truth

Redis
→ Cache

OpenSearch
→ Search Index

RabbitMQ
→ Event Delivery
```

RedisやOpenSearchはPostgreSQLのコピーであり、更新処理は必ずPostgreSQLを経由する。

---

# キャッシュ構成

```text
Browser Cache
  ↓
Nginx Cache
  ↓
Redis Cache
  ↓
PostgreSQL
```

## Redis Cache

商品詳細APIをRedisにキャッシュする。

```text
GET /api/products/{id}
↓
Redis Hit
  → Redisから返却

Redis Miss
  → PostgreSQLから取得
  → Redisへ保存
```

更新・削除時はCache EvictでRedisキャッシュを削除する。

## HTTP Cache

商品詳細APIにETag / Cache-Controlを付与する。

```text
ETag
→ レスポンスのバージョン管理

Cache-Control
→ クライアント側キャッシュ制御

If-None-Match
→ 変更がなければ304 Not Modified
```

## Nginx Cache

商品詳細GETのみNginxでキャッシュする。

```text
GET /api/products/{id}
↓
Nginx Cache Hit
↓
Spring Bootへ到達せず返却
```

ただしNginx Cacheはアプリ側から削除制御しづらいため、実務では慎重に扱う。

---

# 商品更新時の非同期処理

商品更新時には、OpenSearchのIndex更新を同期実行せず、RabbitMQを利用して非同期化する。

```text
PUT /api/products/{id}
  ↓
PostgreSQL更新
  ↓
outbox_events保存
  ↓
OutboxPublisher
  ↓
RabbitMQ
  ↓
Consumer
  ↓
OpenSearch更新
```

---

# Outbox Pattern

DB更新とイベント保存を同一Transactionで行う。

```text
Product更新
+
OutboxEvent保存
```

これにより、DB更新成功後にRabbitMQ送信へ失敗してイベントが消失する問題を防ぐ。

---

# Idempotency

RabbitMQはAt Least Once Deliveryのため、同じイベントが複数回届く可能性がある。

Consumer側では `processed_events` テーブルを使い、処理済みeventIdを管理する。

```text
eventId確認
↓
未処理なら実行
↓
processed_eventsへ保存

処理済みならskip
```

---

# Retry / DLQ

Consumer処理に失敗した場合はRetryする。

Retryしても失敗する場合はDLQへ退避する。

```text
Queue
↓
Consumer失敗
↓
Retry
↓
Retry
↓
Retry
↓
DLQ
```

DLQは失敗イベントの調査・再処理のために利用する。

---

# OpenSearch連携

OpenSearchは商品検索用のIndexとして利用する。

```text
PostgreSQL
→ 正本

OpenSearch
→ 検索用コピー
```

商品更新・削除イベントをConsumerが受け取り、OpenSearchのDocumentを更新・削除する。

既存データはReindex APIで再構築できる。

```text
POST /api/admin/search/reindex
```

---

# 認証・認可

Spring Security + JWTで認証・認可を行う。

```text
Login
↓
Access Token / Refresh Token発行
↓
Authorization: Bearer <token>
↓
JwtAuthenticationFilter
↓
SecurityContext
↓
Role判定
```

## Role

| Role  | 権限                   |
| ----- | -------------------- |
| USER  | ログインユーザー情報取得         |
| ADMIN | 商品作成・更新・削除 / Reindex |

## API制御

```text
GET /api/products/**
→ 公開

POST /api/products
PUT /api/products/{id}
DELETE /api/products/{id}
POST /api/admin/**
→ ADMINのみ

GET /api/users/me
→ USER / ADMIN
```

---

# 可用性・運用

## Health Check

Actuatorで主要コンポーネントの状態を確認する。

```text
/actuator/health
```

対象:

* PostgreSQL
* Redis
* RabbitMQ
* OpenSearch
* Disk

## Metrics

Actuator MetricsでHTTP、JVM、HikariCPなどを確認する。

```text
/actuator/metrics
```

## Circuit Breaker

OpenSearch検索処理にCircuit Breakerを適用する。

```text
OpenSearch失敗増加
↓
Circuit OPEN
↓
OpenSearchを呼ばずfallback
↓
検索APIは空配列を返す
```

これにより、OpenSearch障害時も商品一覧・商品詳細などの主要APIへの影響を抑える。

## Rate Limiter

商品検索APIにRate Limiterを適用し、短時間の大量リクエストからAPIを保護する。

```text
制限超過
↓
429 Too Many Requests
```

---

# スケーリング上の考慮

## Redis Distributed Lock

複数インスタンスでOutboxPublisherが同時実行されることを防ぐため、Redis Distributed Lockを利用する。

```text
App1
App2
↓
Redis Lockを取得できたAppのみOutboxPublisher実行
```

## Read Replica

読み取り負荷が増えた場合、PostgreSQLのRead Replicaを検討する。

```text
Write
→ Primary

Read
→ Replica
```

ただしReplication Lagによる一時的な不整合を考慮する。

## Kubernetes

Kubernetesは概要理解に留める。

本プロジェクトでは以下を学習対象とする。

* Deployment
* Service
* ConfigMap
* Pod
* replicas

---

# 設計上の重要ポイント

## 整合性

PostgreSQLを正本とし、Redis / OpenSearchは一時的な不整合を許容する。

## 可用性

Redis Cache、RabbitMQ、Circuit Breaker、Rate Limiterにより、障害時の影響範囲を小さくする。

## 拡張性

非同期処理と検索基盤を分離することで、将来的に通知、ランキング、分析などを追加しやすくする。

---

# 今後の発展項目

* OAuth2 / OIDC
* Kafka
* Kubernetes本格導入
* Prometheus / Grafana
* 日本語Analyzer
* Sharding
* Distributed Tracing
