package com.example.ecdemo.common.config;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {

    @Bean
    public RestHighLevelClient openSearchClient(
            @Value("${opensearch.host}") String host,
            @Value("${opensearch.port}") int port
    ) {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, port, "http")
                ).setRequestConfigCallback(requestConfigBuilder ->
                        requestConfigBuilder
                                .setConnectTimeout(1000)
                                .setSocketTimeout(2000)
                )
        );
    }
}
