package com.example.ecdemo.product.search;

import com.example.ecdemo.common.exception.TooManyRequestsException;
import com.example.ecdemo.product.entity.Product;
import com.example.ecdemo.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;

import org.opensearch.action.delete.DeleteRequest;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private static final String INDEX_NAME = "products";

    private final RestHighLevelClient openSearchClient;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    public void index(ProductDocument document) {
        try {
            String json = objectMapper.writeValueAsString(document);

            IndexRequest request = new IndexRequest(INDEX_NAME)
                    .id(String.valueOf(document.id()))
                    .source(json, XContentType.JSON);

            openSearchClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to index product document", e);
        }
    }

    // @CircuitBreaker(
    //     name = "openSearch",
    //     fallbackMethod = "fallbackSearch"
    // )
    @RateLimiter(
        name = "productSearch",
        fallbackMethod = "fallbackSearchRateLimit"
    )
    public List<ProductDocument> search(String keyword) {
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                    .query(QueryBuilders.multiMatchQuery(
                            keyword,
                            "name",
                            "description",
                            "categoryName"
                    ));

            SearchRequest request = new SearchRequest(INDEX_NAME)
                    .source(sourceBuilder);

            SearchResponse response = openSearchClient.search(
                    request,
                    RequestOptions.DEFAULT
            );

            return Arrays.stream(response.getHits().getHits())
                    .map(this::toDocument)
                    .toList();

        } catch (IOException e) {
            throw new IllegalStateException("Failed to search product documents", e);
        }
    }

    private ProductDocument toDocument(SearchHit hit) {
        try {
            return objectMapper.readValue(
                    hit.getSourceAsString(),
                    ProductDocument.class
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to deserialize product document", e);
        }
    }

    public void reindexAll() {
        List<Product> products = productRepository.findAllWithCategory();

        for (Product product : products) {
            index(ProductDocument.from(product));
        }
    }

    public void delete(Long productId) {
        try {
            DeleteRequest request = new DeleteRequest(
                    INDEX_NAME,
                    String.valueOf(productId)
            );

            openSearchClient.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to delete product document", e);
        }
    }

    public List<ProductDocument> fallbackSearch(
            String keyword,
            Throwable e
    ) {
        return List.of();
    }

    public List<ProductDocument> fallbackSearchRateLimit(
            String keyword,
            Throwable e
    ) {
        throw new TooManyRequestsException(
                "Too many search requests",
                e
        );
    }
}
