package com.example.gestion_absences;

public class LoginRequest {
    private String email;  // Remplace 'username' par 'email'
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters et setters
    public String getEmail() {  // Getter pour l'email
        return email;
    }

    public void setEmail(String email) {  // Setter pour l'email
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
