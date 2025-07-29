package br.edu.ifg.trilhadeaprendizadoapims.auth.controller;

import br.edu.ifg.trilhadeaprendizadoapims.auth.dto.AuthDTO;
import br.edu.ifg.trilhadeaprendizadoapims.auth.dto.AuthResponseDTO;
import br.edu.ifg.trilhadeaprendizadoapims.auth.model.Auth;
import br.edu.ifg.trilhadeaprendizadoapims.auth.service.AuthService;
import br.edu.ifg.trilhadeaprendizadoapims.auth.util.Util;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private Util util;

    @PostMapping
    public ResponseEntity<AuthResponseDTO> autenticar(@RequestBody @Valid AuthDTO authDTO) {
        try {
            Auth auth = authService.autenticar(authDTO);
            if (auth.getSenha().equals(util.gerarHashMD5(authDTO.getSenha()))) {
                String token = util.gerarToken(auth.getEmail(), auth.getRole());
                return ResponseEntity.ok(new AuthResponseDTO(true, token, auth.getEmail(), auth.getRole()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDTO(false, "Senha incorreta"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDTO(false, "Usuário não encontrado"));
        }
    }
}
