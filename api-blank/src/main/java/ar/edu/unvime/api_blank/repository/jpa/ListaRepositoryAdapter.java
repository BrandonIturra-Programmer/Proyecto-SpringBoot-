package ar.edu.unvime.api_blank.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.repository.ListaRepository;

@Repository
public class ListaRepositoryAdapter implements ListaRepository {

    private final ListaJpaRepository jpa;
    private final UsuarioJpaRepository usuarioJpa;

    public ListaRepositoryAdapter(ListaJpaRepository jpa, UsuarioJpaRepository usuarioJpa) {
        this.jpa = jpa;
        this.usuarioJpa = usuarioJpa;
    }

    @Override
    public List<Lista> listarTodas() {
        return jpa.findAll().stream().map(this::aDominio).toList(); 
    }

    @Override
    public Optional<Lista> buscarPorId(Long id) {
        return jpa.findById(id).map(this::aDominio); 
    }

    @Override
    public Lista guardar(Lista lista) {
        return aDominio(jpa.save(aEntidad(lista)));
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Lista> buscarPorUsuarioId(Long usuarioId) {
        return jpa.findByUsuarioId(usuarioId).stream().map(this::aDominio).toList();  
    } 

    private Lista aDominio(ListaEntity e) {
        Long usuarioId = e.getUsuario() != null ? e.getUsuario().getId() : null;
        return new Lista(e.getId(), e.getNombre(), usuarioId);
    }

    private ListaEntity aEntidad(Lista l) {
        ListaEntity e = new ListaEntity();
        e.setId(l.id());
        e.setNombre(l.nombre());
        if (l.usuarioId() != null) {
            e.setUsuario(usuarioJpa.getReferenceById(l.usuarioId())); 
        }
        return e; 
    }
} 