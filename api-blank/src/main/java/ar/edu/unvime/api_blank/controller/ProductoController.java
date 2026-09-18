package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.ProductoDto;
import ar.edu.unvime.api_blank.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Endpoints del catalogo de productos externos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene el catalogo de productos consumido desde la API externa DummyJSON")
    public List<ProductoDto> listarProductos() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Busca un producto especifico por su ID en la API externa")
    public ProductoDto obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }
}