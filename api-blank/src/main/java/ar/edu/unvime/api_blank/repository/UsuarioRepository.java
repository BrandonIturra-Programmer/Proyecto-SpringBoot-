package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    boolean existePorEmail(String email);
    Usuario guardar(Usuario usuario);
}
