package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.FavoritoRequestDto;
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@Tag(name = "Favoritos", description = "CRUD guardado en la base. Requieren autenticación")
public class FavoritoController {

    private final FavoritoService service;

    public FavoritoController(FavoritoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar favoritos", description = "Devuelve todos los favoritos guardados (Código 200)")
    public List<Favorito> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un favorito", description = "Busca un favorito por ID (Código 200)")
    public Favorito obtener(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Crear favorito", description = "Crea y guarda un nuevo favorito (Código 201)")
    public ResponseEntity<Favorito> crear(@Valid @RequestBody FavoritoRequestDto dto) {
        Favorito nuevo = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar favorito", description = "Actualiza un favorito existente (Código 200)")
    public Favorito actualizar(@PathVariable Long id, @Valid @RequestBody FavoritoRequestDto dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar favorito", description = "Elimina un favorito por ID (Código 204)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}