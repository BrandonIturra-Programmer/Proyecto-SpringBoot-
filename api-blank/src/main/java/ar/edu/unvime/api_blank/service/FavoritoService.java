package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.FavoritoRequestDto;
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import ar.edu.unvime.api_blank.repository.ListaRepository;
import ar.edu.unvime.api_blank.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository repository;
    private final ListaRepository listaRepository;

    public FavoritoService(FavoritoRepository repository, ListaRepository listaRepository) {
        this.repository = repository;        
        this.listaRepository = listaRepository;
    }

    public List<Favorito> listar() {
        return repository.listarTodos();
    }

    public Favorito buscarPorId(Long id) {
        return repository.buscarPorId(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Favorito no encontrado con ID: " + id));
    }

    public Favorito crear(FavoritoRequestDto dto) {
        validarLista(dto.getListaId()); 
        Favorito favorito = new Favorito(null, dto.getProductoId(), dto.getNotaPersonal(), LocalDate.now(), dto.getListaId());
        return repository.guardar(favorito);
    }

    public Favorito actualizar(Long id, FavoritoRequestDto dto) {
        validarLista(dto.getListaId());
        Favorito existente = buscarPorId(id);
        existente.setProductoId(dto.getProductoId());
        existente.setNotaPersonal(dto.getNotaPersonal());
        existente.setListaId(dto.getListaId());
        return repository.guardar(existente);
    }

    public void eliminar(Long id) {
        buscarPorId(id); 
        repository.eliminar(id);
    }

    private void validarLista(Long listaId) {
        listaRepository.buscarPorId(listaId)
        .orElseThrow(() -> new RecursoNoEncontradoException("Lista no encontrada con ID: " + listaId)); 
    }
}