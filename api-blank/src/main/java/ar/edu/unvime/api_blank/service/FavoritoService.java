package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.FavoritoRequestDto;
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository repository;

    public FavoritoService(FavoritoRepository repository) {
        this.repository = repository;
    }

    public List<Favorito> listar() {
        return repository.listarTodos();
    }

    public Favorito buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Favorito no encontrado con ID: " + id));
    }

    public Favorito crear(FavoritoRequestDto dto) {
        Favorito favorito = new Favorito(null, dto.getProductoId(), dto.getNotaPersonal(), LocalDate.now());
        return repository.guardar(favorito);
    }

    public Favorito actualizar(Long id, FavoritoRequestDto dto) {
        Favorito existente = buscarPorId(id);
        existente.setProductoId(dto.getProductoId());
        existente.setNotaPersonal(dto.getNotaPersonal());
        return repository.guardar(existente);
    }

    public void eliminar(Long id) {
        buscarPorId(id); // Lanza excepción si no existe
        repository.eliminar(id);
    }
}