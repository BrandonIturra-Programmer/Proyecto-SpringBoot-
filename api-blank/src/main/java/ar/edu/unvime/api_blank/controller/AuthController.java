package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.RegistroRequestDto;
import ar.edu.unvime.api_blank.dto.UsuarioResponseDto;
import ar.edu.unvime.api_blank.model.Usuario;
import ar.edu.unvime.api_blank.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro e inicio de sesión (endpoints públicos)")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar usuario", description = "Crea un usuario con rol USER (Código 201). Si el email ya existe responde 409")
    public ResponseEntity<UsuarioResponseDto> registro(@Valid @RequestBody RegistroRequestDto dto) {
        Usuario u = usuarioService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioResponseDto(u.id(), u.email(), u.rol()));
    }
}