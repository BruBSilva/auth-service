package br.edu.ifg.trilhadeaprendizadoapims.auth.service;

import br.edu.ifg.trilhadeaprendizadoapims.auth.dto.AuthDTO;
import br.edu.ifg.trilhadeaprendizadoapims.auth.model.Auth;
import br.edu.ifg.trilhadeaprendizadoapims.auth.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Util util;

    public Auth autenticar(AuthDTO authDTO) {
        try {
            return restTemplate.getForObject(
                    "http://localhost:8080/usuario/aluno/email/" + authDTO.getEmail(),
                    Auth.class
            );
        } catch (HttpClientErrorException.NotFound notFoundAluno) {
            try {
                return restTemplate.getForObject(
                        "http://localhost:8080/usuario/admin/email/" + authDTO.getEmail(),
                        Auth.class
                );
            } catch (HttpClientErrorException.NotFound notFoundAdm) {
                throw new RuntimeException("Usuário não encontrado: " + authDTO.getEmail());
            }
        }
    }
}
