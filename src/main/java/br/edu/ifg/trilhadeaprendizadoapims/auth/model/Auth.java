package br.edu.ifg.trilhadeaprendizadoapims.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
    private String email;
    private String senha;
    private String role;
}
