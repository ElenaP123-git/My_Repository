package app.model;

import java.time.LocalDateTime;

public class Usuario {

    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime fechaRegistro;

    public Usuario() {}

    public Usuario(String username, String email, String passwordHash) {
        this.username     = username;
        this.email        = email;
        this.passwordHash = passwordHash;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }
}