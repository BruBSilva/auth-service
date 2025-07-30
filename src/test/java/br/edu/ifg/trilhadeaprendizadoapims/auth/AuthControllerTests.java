package br.edu.ifg.trilhadeaprendizadoapims.auth;

import br.edu.ifg.trilhadeaprendizadoapims.auth.controller.AuthController;
import br.edu.ifg.trilhadeaprendizadoapims.auth.dto.AuthDTO;
import br.edu.ifg.trilhadeaprendizadoapims.auth.model.Auth;
import br.edu.ifg.trilhadeaprendizadoapims.auth.service.AuthService;
import br.edu.ifg.trilhadeaprendizadoapims.auth.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private Util util;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveAutenticarComSucesso() throws Exception {
        AuthDTO authDTO = new AuthDTO("teste@email.com", "senha123");
        Auth auth = new Auth();
        auth.setEmail("teste@email.com");
        auth.setSenha("hash-da-senha");
        auth.setRole("ALUNO");

        when(authService.autenticar(any())).thenReturn(auth);
        when(util.gerarHashMD5(eq("senha123"))).thenReturn("hash-da-senha");
        when(util.gerarToken(eq("teste@email.com"), eq("ALUNO"))).thenReturn("token-jwt");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token").value("token-jwt"));
    }

    @Test
    void deveRetornar401QuandoSenhaIncorreta() throws Exception {
        AuthDTO authDTO = new AuthDTO("teste@email.com", "senhaErrada");
        Auth auth = new Auth();
        auth.setEmail("teste@email.com");
        auth.setSenha("senha-certa-hash");
        auth.setRole("ALUNO");

        when(authService.autenticar(any())).thenReturn(auth);
        when(util.gerarHashMD5("senhaErrada")).thenReturn("hash-errado");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Senha incorreta"));
    }

    @Test
    void deveRetornar401QuandoUsuarioNaoEncontrado() throws Exception {
        AuthDTO authDTO = new AuthDTO("naoexiste@email.com", "senha");

        when(authService.autenticar(any())).thenThrow(new RuntimeException("Usuário não encontrado"));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));
    }
}
