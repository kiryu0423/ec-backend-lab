# 設計判断記録（Architecture Decision Records）

本ドキュメントは、本プロジェクトにおける主要な設計判断とその理由を記録する。

---

# ADR-001 FlywayによるDBマイグレーション管理

## 日付

2026-06-08

## 背景

データベーススキーマをコード管理し、ローカル環境・検証環境・本番環境で同一の状態を再現したい。

## 決定

Flywayを利用してDBマイグレーションを管理する。

## 理由

* SQLをGitで管理できる
* 環境差異を防止できる
* DB構築手順を自動化できる
* スキーマ変更履歴を追跡できる

## 影響

* 実行済みMigrationファイルは原則変更しない
* スキーマ変更時は新しいMigrationを追加する

---

# ADR-002 EntityとDTOを分離する

## 日付

2026-06-08

## 背景

Entityを直接APIレスポンスとして返却すると、DB構造とAPI仕様が密結合になる。

## 決定

Request DTOおよびResponse DTOを利用する。

## 理由

* API仕様とDB構造を分離できる
* 不要な項目の公開を防げる
* Entity変更時の影響範囲を限定できる
* Lazy Loadingによる問題を回避しやすい

## 影響

* DTO変換処理が必要になる
* API変更が容易になる

---

# ADR-003 Open Session In Viewを無効化する

## 日付

2026-06-08

## 背景

Controller層でLazy Loadingが発生すると、意図しないSQL実行やN+1問題を引き起こす可能性がある。

## 決定

spring.jpa.open-in-view=false を設定する。

## 理由

* Service層で取得範囲を明示できる
* Fetch JoinやDTO Projectionを意識した実装になる
* N+1問題を早期に発見できる

## 影響

* Controller層でEntityを直接扱えない
* 必要な関連EntityはService層で事前取得する

---

# ADR-004 商品一覧APIはDTO Projectionを採用する

## 日付

2026-06-08

## 背景

商品一覧画面では商品の一部情報のみが必要であり、Entity全体を取得する必要はない。

## 決定

JPQLのDTO Projectionを利用する。

## 理由

* 不要なデータ取得を防げる
* SQL結果サイズを削減できる
* N+1問題を回避しやすい
* 一覧APIのパフォーマンス向上が期待できる

## 影響

* DTO変更時にJPQL修正が必要になる

---

# ADR-005 商品詳細APIはFetch Joinを採用する

## 日付

2026-06-08

## 背景

商品詳細取得時にはカテゴリ情報も必要となる。

## 決定

商品詳細取得ではFetch Joinを利用する。

## 理由

* 関連Entityを1回のSQLで取得できる
* LazyInitializationExceptionを防止できる
* N+1問題を回避できる

## 影響

* 関連Entityの取得範囲を明示的に管理する必要がある

---

# ADR-006 作成日時・更新日時はAuditingで管理する

## 日付

2026-06-08

## 背景

createdAtおよびupdatedAtをアプリケーション側で手動管理すると実装漏れが発生しやすい。

## 決定

Spring Data JPA Auditingを利用する。

## 理由

* 作成日時・更新日時を自動管理できる
* 更新漏れを防止できる
* Entity実装を簡潔に保てる

## 影響

* Auditing設定が必要になる
* BaseEntityを継承して利用する

---

# ADR-007 PostgreSQLを正本データとして扱う

## 日付

2026-06-08

## 背景

今後RedisやElasticsearchを導入する予定がある。

## 決定

PostgreSQLをシステムの正本データ（Source of Truth）とする。

## 理由

* トランザクション管理が可能
* データ整合性を維持しやすい
* キャッシュや検索エンジンとの責務を分離できる

## 影響

* Redisはキャッシュとして扱う
* Elasticsearchは検索用コピーとして扱う
* 更新処理は必ずPostgreSQLを経由する


# ADR-008 RabbitMQによる非同期処理を採用する

## 背景

商品更新時に検索Index更新などの副作用を同期実行すると、APIレスポンスが遅くなる可能性がある。

## 決定

RabbitMQを利用して非同期処理を行う。

## 理由

- 商品更新処理と検索Index更新を分離できる
- 外部システム障害の影響を小さくできる
- Retry / DLQ による失敗処理を設計できる

---

# ADR-009 Outbox Patternを採用する

## 背景

DB更新後にRabbitMQ送信へ失敗すると、イベントが消失する可能性がある。

## 決定

DB更新とOutbox保存を同一Transactionで行い、別PublisherがRabbitMQへ送信する。

## 理由

- DB更新とイベント保存の整合性を保てる
- RabbitMQ停止時もイベントを失わない
- 後続処理を再送できる

---

# ADR-010 Consumer側で冪等性を担保する

## 背景

RabbitMQはAt Least Once配送のため、同じイベントが複数回届く可能性がある。

## 決定

processed_events テーブルで処理済みeventIdを管理する。

## 理由

- 重複イベントによる二重処理を防げる
- 再送に強いConsumer設計にできる

---

# ADR-011 OpenSearchを検索用コピーとして利用する

## 背景

PostgreSQLのLIKE検索だけでは、検索要件が増えた場合に限界がある。

## 決定

OpenSearchを商品検索用Indexとして利用する。

## 理由

- 商品検索をDB負荷から分離できる
- 検索専用のDocument設計ができる
- Reindex APIによりDBから再構築できる

## 影響

- PostgreSQLとOpenSearchの間に一時的な不整合が発生する
- Outbox + RabbitMQ により最終的整合性を目指す

# ADR-012 Spring Securityを採用する

## 背景

商品作成・更新・削除などの管理系APIを誰でも実行できる状態は危険である。

## 決定

Spring Securityを導入し、認証・認可を行う。

## 理由

- API単位でアクセス制御できる
- USER / ADMIN の権限管理ができる
- JWT認証へ拡張しやすい

## 影響

- SecurityConfigによるルーティング制御が必要になる
- 401 / 403 のエラーハンドリングが必要になる

---

# ADR-013 JWTによるステートレス認証を採用する

## 背景

API認証では、サーバー側でSessionを持たずに認証情報を扱いたい。

## 決定

Access TokenとしてJWTを利用する。

## 理由

- サーバー側でSessionを保持しない
- APIリクエストごとに認証情報を検証できる
- フロントエンドや外部クライアントと連携しやすい

## 影響

- JWTの署名鍵・有効期限管理が必要になる
- Token漏洩時のリスクを考慮する必要がある

---

# ADR-014 Refresh TokenをDBで管理する

## 背景

Access Tokenは短時間で期限切れにしつつ、ユーザーに頻繁な再ログインを要求しないようにしたい。

## 決定

Refresh TokenをDBに保存し、Access Token再発行に利用する。

## 理由

- Refresh Tokenをサーバー側で無効化できる
- Logout時にRefresh Tokenを削除できる
- 将来的にDevice管理やToken Rotationへ拡張できる

## 影響

- refresh_tokens テーブルが必要になる
- Refresh Tokenの有効期限管理が必要になる

---

# ADR-015 Role-Based Access Controlを採用する

## 背景

一般ユーザーと管理者で利用できるAPIを分けたい。

## 決定

USER / ADMIN のRoleを定義し、APIごとに認可制御を行う。

## 理由

- 商品閲覧は公開できる
- 商品作成・更新・削除はADMINのみに制限できる
- 権限不足時は403 Forbiddenを返せる

## 影響

- usersテーブルにroleを保持する
- JWTにrole claimを含める

# ADR-016 ActuatorによるHealth Check / Metricsを導入する

## 背景

アプリケーションやミドルウェアの状態を外部から確認できるようにしたい。

## 決定

Spring Boot Actuatorを導入する。

## 理由

- DB / Redis / RabbitMQ / OpenSearch の疎通状態を確認できる
- HTTPリクエスト、JVM、HikariCPなどのMetricsを確認できる
- 障害調査の入口になる

## 影響

- /actuator/health などのエンドポイントを公開する
- SecurityConfigでActuatorへのアクセス制御を行う

---

# ADR-017 OpenSearchにCircuit Breakerを適用する

## 背景

OpenSearch停止時に検索APIが毎回失敗し、アプリケーション側のリソースを消費し続ける可能性がある。

## 決定

Resilience4j Circuit BreakerをOpenSearch検索処理に適用する。

## 理由

- OpenSearch障害時に呼び出しを遮断できる
- 検索APIだけを縮退運転できる
- 商品一覧・商品詳細などの主要APIへの影響を抑えられる

## 影響

- OpenSearch障害時は検索結果として空配列を返す
- Circuit Breakerの状態はActuatorで確認する
