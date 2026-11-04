package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.model.Favorito;
import java.util.List;
import java.util.Optional;

public interface FavoritoRepository {
    List<Favorito> listarTodos();
    List<Favorito> buscarPorListaId(Long listaId);
    Optional<Favorito> buscarPorId(Long id); 
    Favorito guardar(Favorito favorito);
    void eliminar(Long id);
}