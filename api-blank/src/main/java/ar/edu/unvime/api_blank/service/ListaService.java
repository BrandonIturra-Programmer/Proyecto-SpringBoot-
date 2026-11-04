package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.ListaRequestDto;
import ar.edu.unvime.api_blank.exception.ConflictoException;
import ar.edu.unvime.api_blank.exception.RecursoNoEncontradoException;
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import ar.edu.unvime.api_blank.repository.ListaRepository;
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

    public List<Lista> listar() {
        return repository.listarTodas();
    }

    public List<Favorito> favoritosDeLista(Long id) {
        buscarPorId(id);
        return favoritoRepository.buscarPorListaId(id);
    }

    public Lista buscarPorId(Long id) {
        return repository.buscarPorId(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Lista no encontrada con ID: " + id));
    }

    public Lista crear(ListaRequestDto dto) {
        return repository.guardar(new Lista(null, dto.getNombre()));
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        if (!favoritoRepository.buscarPorListaId(id).isEmpty()) {
            throw new ConflictoException("No se puede eliminar la lista " + id + " porque tiene favoritos");
        }
        repository.eliminar(id); 
    } 

    @Transactional 
    public void moverFavoritos(Long origenId, Long destinoId) {
        buscarPorId(origenId);   
        buscarPorId(destinoId); 
         
        if (origenId.equals(destinoId)) {
            throw new ConflictoException("La lista origen y la destino no pueden ser la misma");
        }

    
        for (Favorito f : favoritoRepository.buscarPorListaId(origenId)) {
            f.setListaId(destinoId);
            favoritoRepository.guardar(f);
        }
        
        repository.eliminar(origenId);
    }

} 