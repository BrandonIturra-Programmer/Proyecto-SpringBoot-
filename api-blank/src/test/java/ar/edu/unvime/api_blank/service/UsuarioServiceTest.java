package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.RegistroRequestDto;
import ar.edu.unvime.api_blank.model.Usuario;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    private final UsuarioRepository repository = mock(UsuarioRepository.class);
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UsuarioService service = new UsuarioService(repository, encoder);

    @Test
    void registrarNuncaGuardaLaContrasenaEnTextoPlano() {
        RegistroRequestDto dto = new RegistroRequestDto();
        dto.setEmail("ana@test.com");
        dto.setPassword("123456");
        when(repository.existePorEmail("ana@test.com")).thenReturn(false);
        when(repository.guardar(any(Usuario.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        service.registrar(dto);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).guardar(captor.capture());
        Usuario guardado = captor.getValue();

        assertNotEquals("123456", guardado.passwordHash());
        assertTrue(encoder.matches("123456", guardado.passwordHash()));
    }
}