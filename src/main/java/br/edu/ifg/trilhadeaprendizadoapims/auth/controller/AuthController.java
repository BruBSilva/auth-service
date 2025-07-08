package br.edu.ifg.trilhadeaprendizadoapims.auth.controller;

import br.edu.ifg.trilhadeaprendizadoapims.auth.dto.AuthDTO;
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
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<String> autenticar(@RequestBody @Valid AuthDTO authDTO) {
        Auth auth = authService.autenticar(authDTO);
        if (auth.getSenha().equals(Util.gerarHashMD5(authDTO.getSenha()))) {
            String token = Util.gerarToken(auth.getEmail(), auth.getRole());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
        }

    }
}
