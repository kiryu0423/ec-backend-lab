# 学習メモ

## キャッシュ

Redis、ETag、Cache-Control、Nginx Cacheを実装した。

### 学んだこと

- RedisはDB負荷を減らす
- Nginx CacheはSpring Bootへの到達を減らす
- Cache-Controlはブラウザ側で通信自体を減らす
- キャッシュは速くなる一方で不整合リスクが増える

---

## 非同期処理

RabbitMQ、Retry、DLQ、Idempotency、Outbox Patternを実装した。

### 学んだこと

- RabbitMQはAt Least Once Delivery
- 同じイベントが複数回届く可能性がある
- Consumer側で冪等性が必要
- DB更新とイベント送信は同一Transactionにできない
- Outbox Patternでイベント消失を防げる

---

## 検索

OpenSearchを導入した。

### 学んだこと

- PostgreSQLを正本、OpenSearchを検索用コピーとして扱う
- 更新・削除イベントで非同期にIndexを同期する
- Reindex APIで検索Indexを再構築できる
- 検索基盤は一時的な不整合を許容する

---

## 認証・認可

Spring Security、JWT、Refresh Token、RBACを実装した。

### 学んだこと

- JWTはステートレス認証
- Security FilterでJWTを検証する
- SecurityContextに認証情報を設定する
- 401と403は役割が違う
- Refresh TokenをDB管理するとLogoutや無効化ができる

---

## 可用性

Actuator、Health Check、Metrics、Circuit Breaker、Rate Limiterを実装した。

### 学んだこと

- Health Checkで依存先の状態を確認できる
- MetricsでHTTP/JVM/DB接続状況を見られる
- Circuit Breakerで障害依存先への呼び出しを遮断できる
- Rate Limiterで過剰アクセスからAPIを守れる

---

## スケーリング

Redis Distributed Lock、Read Replica、Kubernetes概要を学んだ。

### 学んだこと

- 複数インスタンスでは定期実行の多重起動に注意する
- Redis LockでOutboxPublisherの同時実行を防げる
- Read Replicaは読み取り負荷分散に有効
- Replica Lagによる不整合を考慮する必要がある
- Kubernetesはアプリ運用・スケール・自己修復の仕組み
