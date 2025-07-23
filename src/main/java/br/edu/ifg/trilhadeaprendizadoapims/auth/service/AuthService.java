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

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + util.gerarToken(authDTO.getEmail()));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            // Try to fetch as student first
            ResponseEntity<Auth> response = restTemplate.exchange(
                    "http://localhost:8080/usuario/aluno/email/" + authDTO.getEmail(),
                    HttpMethod.GET,
                    entity,
                    Auth.class
            );
            
            Auth auth = response.getBody();
            auth.setRole("ALUNO");
            return auth;
            
        } catch (HttpClientErrorException.NotFound notFoundAluno) {
            try {
                // Try to fetch as admin
                ResponseEntity<Auth> response = restTemplate.exchange(
                        "http://localhost:8080/usuario/admin/email/" + authDTO.getEmail(),
                        HttpMethod.GET,
                        entity,
                        Auth.class
                );
                
                Auth auth = response.getBody();
                auth.setRole("ADMIN");
                return auth;
                
            } catch (HttpClientErrorException.NotFound notFoundAdm) {
                throw new RuntimeException("User not found: " + authDTO.getEmail());
            }
        }
    }
}
