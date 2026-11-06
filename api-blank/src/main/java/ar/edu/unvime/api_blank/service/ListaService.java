package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.ListaRequestDto;
import ar.edu.unvime.api_blank.exception.AccesoDenegadoException;
import ar.edu.unvime.api_blank.exception.ConflictoException;
import ar.edu.unvime.api_blank.exception.RecursoNoEncontradoException;
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.model.Rol;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import ar.edu.unvime.api_blank.repository.ListaRepository;
import ar.edu.unvime.api_blank.security.UsuarioAutenticado;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListaService {

    private final ListaRepository repository;
    private final FavoritoRepository favoritoRepository;

    public ListaService(ListaRepository repository, FavoritoRepository favoritoRepository) {
        this.repository = repository;
        this.favoritoRepository = favoritoRepository;
    }

    public List<Lista> listar(UsuarioAutenticado usuario) {
        if (usuario.rol() == Rol.ADMIN) {
            return repository.listarTodas(); 
        }
        return repository.buscarPorUsuarioId(usuario.id());
    } 

    public List<Favorito> favoritosDeLista(Long id, UsuarioAutenticado usuario) {
        obtenerAutorizada(id, usuario); 
        return favoritoRepository.buscarPorListaId(id);
    }

    public Lista buscarPorId(Long id) {
        return repository.buscarPorId(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Lista no encontrada con ID: " + id));
    }

    public Lista crear(ListaRequestDto dto, UsuarioAutenticado usuario) {
        return repository.guardar(new Lista(null, dto.getNombre(), usuario.id()));
    }

    public void eliminar(Long id, UsuarioAutenticado usuario) {
        obtenerAutorizada(id, usuario);
        if (!favoritoRepository.buscarPorListaId(id).isEmpty()) {
            throw new ConflictoException("No se puede eliminar la lista " + id + " porque tiene favoritos");
        }
        repository.eliminar(id); 
    }

    public Lista obtener(Long id, UsuarioAutenticado usuario) {
        return obtenerAutorizada(id, usuario);
    }

    @Transactional 
    public void moverFavoritos(Long origenId, Long destinoId, UsuarioAutenticado usuario) {
        obtenerAutorizada(origenId, usuario); 
        obtenerAutorizada(destinoId, usuario);
         
        if (origenId.equals(destinoId)) {
            throw new ConflictoException("La lista origen y la destino no pueden ser la misma");
        }

    
        for (Favorito f : favoritoRepository.buscarPorListaId(origenId)) {
            f.setListaId(destinoId);
            favoritoRepository.guardar(f);
        }
        
        repository.eliminar(origenId);
    }
    
    private Lista obtenerAutorizada(Long id, UsuarioAutenticado usuario) {
        Lista lista = buscarPorId(id); // 404 si no existe
        boolean esAdmin = usuario.rol() == Rol.ADMIN; 
        boolean esDueno = usuario.id().equals(lista.usuarioId());
    
        if (!esAdmin && !esDueno) {
            throw new AccesoDenegadoException("No tenés permiso para acceder a la lista " + id);
        }
        return lista;

    }
} 