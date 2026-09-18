package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.model.Favorito;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FavoritoRepository {
    private final List<Favorito> favoritos = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(0);

    public List<Favorito> listarTodos() {
        return favoritos;
    }

    public Optional<Favorito> buscarPorId(Long id) {
        return favoritos.stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    public Favorito guardar(Favorito favorito) {
        if (favorito.getId() == null) {
            favorito.setId(contadorId.incrementAndGet());
        } else {
            favoritos.removeIf(f -> f.getId().equals(favorito.getId()));
        }
        favoritos.add(favorito);
        return favorito;
    }

    public void eliminar(Long id) {
        favoritos.removeIf(f -> f.getId().equals(id));
    }
}