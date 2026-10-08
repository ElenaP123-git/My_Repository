package app.model;

import java.time.LocalDateTime;

public class Idea {

    private int id;
    private int usuarioId;
    private String ideaOriginal;
    private String ideaExpandida;
    private LocalDateTime fecha;

    public Idea() {}

    public Idea(int usuarioId, String ideaOriginal) {
        this.usuarioId    = usuarioId;
        this.ideaOriginal = ideaOriginal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getIdeaOriginal() { return ideaOriginal; }
    public void setIdeaOriginal(String ideaOriginal) { this.ideaOriginal = ideaOriginal; }

    public String getIdeaExpandida() { return ideaExpandida; }
    public void setIdeaExpandida(String ideaExpandida) { this.ideaExpandida = ideaExpandida; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return "Idea{id=" + id + ", original='" + ideaOriginal + "'}";
    }
}