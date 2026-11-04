package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.client.DummyJsonClient;
import ar.edu.unvime.api_blank.dto.ProductoDto;
import ar.edu.unvime.api_blank.exception.RecursoNoEncontradoException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final DummyJsonClient dummyJsonClient;

    public ProductoService(DummyJsonClient dummyJsonClient) {
        this.dummyJsonClient = dummyJsonClient;
    }

    public List<ProductoDto> obtenerTodos() {
        List<Map<String, Object>> rawProducts = dummyJsonClient.obtenerProductosExternos();
        return rawProducts.stream().map(this::mapearADto).collect(Collectors.toList());
    }

    public ProductoDto obtenerPorId(Long id) {
        Map<String, Object> rawProduct = dummyJsonClient.obtenerProductoPorId(id);
        if (rawProduct == null || rawProduct.isEmpty()) {
            throw new RecursoNoEncontradoException("Producto no encontrado en la API externa");
        }
        return mapearADto(rawProduct);
    }

    private ProductoDto mapearADto(Map<String, Object> raw) {
        ProductoDto dto = new ProductoDto();
        dto.setId(raw.get("id") != null ? Long.valueOf(raw.get("id").toString()) : null);
        dto.setTitle((String) raw.get("title"));
        dto.setPrice(raw.get("price") != null ? Double.valueOf(raw.get("price").toString()) : 0.0);
        dto.setCategory((String) raw.get("category"));
        return dto;
    }
}