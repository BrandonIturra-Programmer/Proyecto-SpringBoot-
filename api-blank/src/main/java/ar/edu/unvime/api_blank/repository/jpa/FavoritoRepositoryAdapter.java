package ar.edu.unvime.api_blank.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;

@Repository
public class FavoritoRepositoryAdapter implements FavoritoRepository {

    private final FavoritoJpaRepository jpa;
    private final ListaJpaRepository listaJpa;

    public FavoritoRepositoryAdapter(FavoritoJpaRepository jpa, ListaJpaRepository listaJpa) {
        this.jpa = jpa;
        this.listaJpa = listaJpa;
    }

    

    @Override
    public List<Favorito> listarTodos() {
        return jpa.findAll().stream().map(this::aDominio).toList();
    }

    @Override
    public Optional<Favorito> buscarPorId(Long id) {
        return jpa.findById(id).map(this::aDominio);
    }

    @Override
    public Favorito guardar(Favorito favorito) {
        return aDominio(jpa.save(aEntidad(favorito)));
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Favorito> buscarPorListaId(Long listaId) {
        return jpa.findByListaId(listaId).stream().map(this::aDominio).toList();
    }

    private Favorito aDominio(FavoritoEntity e) {
        Long listaId = e.getLista() != null ? e.getLista().getId() : null;
        return new Favorito(e.getId(), e.getProductoId(), e.getNota(), e.getFechaAlta(), listaId);
    }

    private FavoritoEntity aEntidad(Favorito f) {
        FavoritoEntity e = new FavoritoEntity();
        e.setId(f.getId());
        e.setProductoId(f.getProductoId());
        e.setNota(f.getNotaPersonal());
        e.setFechaAlta(f.getFechaAgregado());
            if (f.getListaId() != null) {
                e.setLista(listaJpa.getReferenceById(f.getListaId()));
            }
        return e;
    }
}