package br.edu.ifg.trilhadeaprendizadoapims.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {
    private String email;
    private String senha;
}
