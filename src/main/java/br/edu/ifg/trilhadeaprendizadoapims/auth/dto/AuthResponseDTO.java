package br.edu.ifg.trilhadeaprendizadoapims.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponseDTO {
    private boolean success;
    private String token;
    private UserDataDTO user;
    private String message;
    
    public AuthResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public AuthResponseDTO(boolean success, String token, UserDataDTO user) {
        this.success = success;
        this.token = token;
        this.user = user;
    }
}
