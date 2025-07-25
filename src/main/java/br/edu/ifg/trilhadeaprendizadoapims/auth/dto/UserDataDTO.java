package br.edu.ifg.trilhadeaprendizadoapims.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDataDTO {
    private Long id;
    private String nome;
    private String email;
    private String role;
    
    private Integer xpTotal;
    private Integer nivel;
    private LocalDateTime dataCadastro;
}
