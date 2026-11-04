package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.ListaRequestDto;
import ar.edu.unvime.api_blank.dto.MoverFavoritosRequestDto; 
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas", description = "Listas para organizar favoritos")
public class ListaController {

    private final ListaService service;

    public ListaController(ListaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crear lista", description = "Crea una nueva lista (Código 201)")
    public ResponseEntity<Lista> crear(@Valid @RequestBody ListaRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    @Operation(summary = "Listar listas", description = "Devuelve todas las listas (Código 200)")
    public List<Lista> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una lista", description = "Busca una lista por ID (Código 200)")
    public Lista obtener(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/{id}/favoritos")
    @Operation(summary = "Favoritos de una lista", description = "Muestra los favoritos de una lista (Código 200)")
    public List<Favorito> favoritos(@PathVariable Long id) {
        return service.favoritosDeLista(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una lista vacía", description = "Elimina una lista sin favoritos. Si tiene favoritos responde 409")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{origenId}/mover-favoritos")
    @Operation(summary = "Mover favoritos", description = "Reasigna todos los favoritos de la lista origen a la destino y elimina la lista origen, en una sola transacción (Código 204)")
    public ResponseEntity<Void> moverFavoritos(@PathVariable Long origenId,@Valid @RequestBody MoverFavoritosRequestDto dto) {
        service.moverFavoritos(origenId, dto.getDestinoId());
        return ResponseEntity.noContent().build();
    }


}