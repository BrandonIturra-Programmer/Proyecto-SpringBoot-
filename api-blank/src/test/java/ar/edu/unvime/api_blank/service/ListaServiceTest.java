package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.ListaRequestDto;
import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.model.Rol;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import ar.edu.unvime.api_blank.repository.ListaRepository;
import ar.edu.unvime.api_blank.security.UsuarioAutenticado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListaServiceTest {

    @Mock
    private ListaRepository listaRepository;

    @Mock
    private FavoritoRepository favoritoRepository;

    @InjectMocks
    private ListaService service;

    @Test
    void crearAsociaLaListaAlUsuarioAutenticado() {
        UsuarioAutenticado usuario = new UsuarioAutenticado(7L, Rol.USER);
        ListaRequestDto dto = new ListaRequestDto();
        dto.setNombre("Regalos");
        when(listaRepository.guardar(any(Lista.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Lista creada = service.crear(dto, usuario); 

        assertEquals(7L, creada.usuarioId());
        assertEquals("Regalos", creada.nombre());
    }
}