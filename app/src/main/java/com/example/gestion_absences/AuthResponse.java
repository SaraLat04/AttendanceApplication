package com.example.gestion_absences;

public class AuthResponse {
    private String access_token;
    private String token_type;
    private String role; // Nouveau champ pour le rôle

    // Constructeur avec tous les champs
    public AuthResponse(String access_token, String token_type, String role) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.role = role;
    }

    // Getters et Setters
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
