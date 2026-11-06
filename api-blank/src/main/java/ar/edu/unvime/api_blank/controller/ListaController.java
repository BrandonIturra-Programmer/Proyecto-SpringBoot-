package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.ListaRequestDto;
import ar.edu.unvime.api_blank.dto.MoverFavoritosRequestDto;
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.security.UsuarioAutenticado;
import ar.edu.unvime.api_blank.service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas", description = "Listas para organizar favoritos. Requieren autenticación")
public class ListaController {

    private final ListaService service;

    public ListaController(ListaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crear lista", description = "Crea una lista cuyo dueño es el usuario autenticado (Código 201)")
    public ResponseEntity<Lista> crear(@Valid @RequestBody ListaRequestDto dto,
                                       @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto, usuario));
    }


    @GetMapping
    @Operation(summary = "Listar listas", description = "Devuelve las listas del usuario") 
    public List<Lista> listar(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.listar(usuario); 
    } 

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una lista", description = "Busca una lista por ID. 403 si no es del usuario (Código 200)")
    public Lista obtener(@PathVariable Long id,
                         @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.obtener(id, usuario);
    }

    @GetMapping("/{id}/favoritos")
    @Operation(summary = "Favoritos de una lista", description = "Devuelve los favoritos de la lista. 403 si no es del usuario (Código 200)")
    public List<Favorito> favoritos(@PathVariable Long id,
                                    @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.favoritosDeLista(id, usuario);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una lista vacía", description = "Elimina una lista sin favoritos (Código 204). Si tiene favoritos responde 409, si no es del usuario 403")
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                         @AuthenticationPrincipal UsuarioAutenticado usuario) {
        service.eliminar(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{origenId}/mover-favoritos")
    @Operation(summary = "Mover favoritos", description = "Reasigna todos los favoritos de la lista origen a la destino y elimina la lista origen, en una sola transacción (Código 204). 403 si alguna lista no es del usuario")
    public ResponseEntity<Void> moverFavoritos(@PathVariable Long origenId,
                                               @Valid @RequestBody MoverFavoritosRequestDto dto,
                                               @AuthenticationPrincipal UsuarioAutenticado usuario) {
        service.moverFavoritos(origenId, dto.getDestinoId(), usuario);
        return ResponseEntity.noContent().build();
    }
}