package app.model;

import java.time.LocalDateTime;

public class Libro {

    private int id;
    private int usuarioId;
    private String titulo;
    private String genero;
    private String descripcion;
    private String estado;
    private String colorPortada;
    private String emojiPortada;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEdicion;

    // Contadores de contenido (no son columnas, se calculan en el repo)
    private String fechaEdicionStr;
    private int numPersonajes;
    private int numNotas;
    private int numImagenes;

    public Libro() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getColorPortada() { return colorPortada; }
    public void setColorPortada(String colorPortada) { this.colorPortada = colorPortada; }

    public String getEmojiPortada() { return emojiPortada; }
    public void setEmojiPortada(String emojiPortada) { this.emojiPortada = emojiPortada; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaEdicion() { return fechaEdicion; }
    public void setFechaEdicion(LocalDateTime fechaEdicion) { this.fechaEdicion = fechaEdicion; }

    public String getFechaEdicionStr() { return fechaEdicionStr; }
    public void setFechaEdicionStr(String s) { this.fechaEdicionStr = s; }

    public int getNumPersonajes() { return numPersonajes; }
    public void setNumPersonajes(int numPersonajes) { this.numPersonajes = numPersonajes; }

    public int getNumNotas() { return numNotas; }
    public void setNumNotas(int numNotas) { this.numNotas = numNotas; }

    public int getNumImagenes() { return numImagenes; }
    public void setNumImagenes(int numImagenes) { this.numImagenes = numImagenes; }
}