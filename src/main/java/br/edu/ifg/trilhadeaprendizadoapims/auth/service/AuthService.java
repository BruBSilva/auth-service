package br.edu.ifg.trilhadeaprendizadoapims.auth.service;

import br.edu.ifg.trilhadeaprendizadoapims.auth.dto.AuthDTO;
import br.edu.ifg.trilhadeaprendizadoapims.auth.model.Auth;
import br.edu.ifg.trilhadeaprendizadoapims.auth.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${gateway.url}")
    private String gatewayUrl;

    public Auth autenticar(AuthDTO authDTO) {

        //Cria os headers com o token de autenticação temporario para a requisição de /auth
        //já que antes de autenticar o usuário, não temos um token válido
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + util.gerarToken(authDTO.getEmail()));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(
                    gatewayUrl + "/usuario/aluno/email/" + authDTO.getEmail(),
                    HttpMethod.GET,
                    entity,
                    Auth.class
            ).getBody();
        } catch (HttpClientErrorException.NotFound notFoundAluno) {
            try {
                return restTemplate.exchange(
                        gatewayUrl + "/usuario/admin/email/" + authDTO.getEmail(),
                        HttpMethod.GET,
                        entity,
                        Auth.class
                ).getBody();
            } catch (HttpClientErrorException.NotFound notFoundAdm) {
                throw new RuntimeException("Usuário não encontrado: " + authDTO.getEmail());
            }
        }
    }
}
