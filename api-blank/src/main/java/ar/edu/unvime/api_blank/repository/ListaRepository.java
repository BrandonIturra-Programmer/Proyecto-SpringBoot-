package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.model.Lista;
import java.util.List;
import java.util.Optional;

public interface ListaRepository {
    List<Lista> listarTodas();
    Optional<Lista> buscarPorId(Long id);
    Lista guardar(Lista lista);
    void eliminar(Long id);
}