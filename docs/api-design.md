# API Design

## Product

### GET /api/products

商品一覧を取得する。

Query:
- page
- size
- categoryId
- keyword
- sort

Response:
- id
- name
- price
- categoryName
- rating
- createdAt

### GET /api/products/{id}

商品詳細を取得する。

Response:
- id
- name
- description
- price
- stock
- category
- createdAt
- updatedAt
- version

### POST /api/products

商品を作成する。

### PUT /api/products/{id}

商品を更新する。

楽観ロック:
- version を利用する

### DELETE /api/products/{id}

商品を削除する。
