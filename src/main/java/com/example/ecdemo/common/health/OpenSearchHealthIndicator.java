package com.example.ecdemo.common.health;

import lombok.RequiredArgsConstructor;
import org.opensearch.client.Request;
import org.opensearch.client.Response;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenSearchHealthIndicator implements HealthIndicator {

    private final RestHighLevelClient openSearchClient;

    @Override
    public Health health() {
        try {
            Response response = openSearchClient
                    .getLowLevelClient()
                    .performRequest(new Request("GET", "/"));

            return Health.up()
                    .withDetail("statusCode", response.getStatusLine().getStatusCode())
                    .build();

        } catch (Exception e) {
            return Health.down(e)
                    .build();
        }
    }
}
