package br.edu.ifg.trilhadeaprendizadoapims.auth;

import br.edu.ifg.trilhadeaprendizadoapims.auth.dto.AuthDTO;
import br.edu.ifg.trilhadeaprendizadoapims.auth.model.Auth;
import br.edu.ifg.trilhadeaprendizadoapims.auth.service.AuthService;
import br.edu.ifg.trilhadeaprendizadoapims.auth.util.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Util util;

    private final String email = "user@email.com";

    @Test
    void autenticar_deveRetornarAluno_QuandoEncontrado() {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(email);

        Auth alunoAuth = new Auth();
        alunoAuth.setEmail(email);

        when(restTemplate.getForObject(
                eq("http://localhost:8080/usuario/aluno/email/" + email),
                eq(Auth.class)
        )).thenReturn(alunoAuth);

        Auth result = authService.autenticar(authDTO);

        assertEquals("ALUNO", result.getRole());
        assertEquals(email, result.getEmail());
    }

    @Test
    void autenticar_deveRetornarAdmin_QuandoAlunoNaoEncontrado() {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(email);

        Auth adminAuth = new Auth();
        adminAuth.setEmail(email);

        when(restTemplate.getForObject(
                eq("http://localhost:8080/usuario/aluno/email/" + email),
                eq(Auth.class)
        )).thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, null, null));

        when(restTemplate.getForObject(
                eq("http://localhost:8080/usuario/admin/email/" + email),
                eq(Auth.class)
        )).thenReturn(adminAuth);

        Auth result = authService.autenticar(authDTO);

        assertEquals("ADMIN", result.getRole());
        assertEquals(email, result.getEmail());
    }

    @Test
    void autenticar_deveLancarExcecao_QuandoNaoEncontradoComoAlunoNemAdmin() {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(email);

        when(restTemplate.getForObject(
                eq("http://localhost:8080/usuario/aluno/email/" + email),
                eq(Auth.class)
        )).thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, null, null));

        when(restTemplate.getForObject(
                eq("http://localhost:8080/usuario/admin/email/" + email),
                eq(Auth.class)
        )).thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, null, null));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.autenticar(authDTO);
        });

        assertEquals("Usuário não encontrado: " + email, exception.getMessage());
    }
}

