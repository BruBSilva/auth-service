package br.edu.ifg.trilhadeaprendizadoapims.auth.service;

import br.edu.ifg.trilhadeaprendizadoapims.auth.dto.AuthDTO;
import br.edu.ifg.trilhadeaprendizadoapims.auth.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    public Auth autenticar(AuthDTO authDTO) {
        try {
            return restTemplate.getForObject("http://localhost:8081/api/aluno/email/%s".formatted(authDTO.getEmail()), Auth.class);
        } catch (HttpClientErrorException.NotFound notFoundAluno) {
            try {
                return restTemplate.getForObject("http://localhost:8081/api/admin/email/%s".formatted(authDTO.getEmail()), Auth.class);
            } catch (HttpClientErrorException.NotFound notFoundAdm) {
                throw new RuntimeException(notFoundAdm.getMessage());
            }
        }
    }
}
