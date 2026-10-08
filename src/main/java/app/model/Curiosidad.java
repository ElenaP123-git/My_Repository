package app.model;

public class Curiosidad {
    private String id;
    private String emoji;
    private String categoria;
    private String titulo;
    private String resumen;
    private String detalle;
    private String preguntaIA;

    public Curiosidad(String id, String emoji, String categoria, String titulo,
                      String resumen, String detalle, String preguntaIA) {
        this.id = id;
        this.emoji = emoji;
        this.categoria = categoria;
        this.titulo = titulo;
        this.resumen = resumen;
        this.detalle = detalle;
        this.preguntaIA = preguntaIA;
    }

    public String getId() { return id; }
    public String getEmoji() { return emoji; }
    public String getCategoria() { return categoria; }
    public String getTitulo() { return titulo; }
    public String getResumen() { return resumen; }
    public String getDetalle() { return detalle; }
    public String getPreguntaIA() { return preguntaIA; }
}