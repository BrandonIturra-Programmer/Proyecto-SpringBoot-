package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.RegistroRequestDto;
import ar.edu.unvime.api_blank.exception.ConflictoException;
import ar.edu.unvime.api_blank.exception.CredencialesInvalidasException;
import ar.edu.unvime.api_blank.model.Rol;
import ar.edu.unvime.api_blank.model.Usuario;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(RegistroRequestDto dto) {
        if (repository.existePorEmail(dto.getEmail())) {
            throw new ConflictoException("Ya existe un usuario con el email " + dto.getEmail());
        }
        String hash = passwordEncoder.encode(dto.getPassword());
        return repository.guardar(new Usuario(null, dto.getEmail(), hash, Rol.USER));
    }

    public Usuario autenticar(String email, String password) {
        Usuario usuario = repository.buscarPorEmail(email)
            .orElseThrow(() -> new CredencialesInvalidasException("Credenciales inválidas")); 
            if (!passwordEncoder.matches(password, usuario.passwordHash())) {
                throw new CredencialesInvalidasException("Credenciales inválidas"); 
            }
        return usuario;
    }
}