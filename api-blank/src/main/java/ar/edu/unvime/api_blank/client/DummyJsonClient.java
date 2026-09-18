package ar.edu.unvime.api_blank.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class DummyJsonClient {

    private final RestClient restClient;

    public DummyJsonClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://dummyjson.com")
                .build();
    }

    public List<Map<String, Object>> obtenerProductosExternos() {
        Map<String, Object> response = restClient.get()
                .uri("/products")
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        if (response != null && response.containsKey("products")) {
            return (List<Map<String, Object>>) response.get("products");
        }
        return List.of();
    }

    public Map<String, Object> obtenerProductoPorId(Long id) {
        return restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}