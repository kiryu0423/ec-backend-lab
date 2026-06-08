# Architecture

## Phase 1

Client
→ Spring Boot
→ PostgreSQL

## Phase 2

Client
→ Nginx
→ Spring Boot
→ Redis
→ PostgreSQL

## Phase 3

Spring Boot
→ RabbitMQ
→ Consumer
→ Elasticsearch

## Data Consistency

- PostgreSQLを正本とする
- Redisはキャッシュ
- Elasticsearchは検索用コピー
- RabbitMQは非同期イベント配送
