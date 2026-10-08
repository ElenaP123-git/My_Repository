package app.model;

public class PiezaAvatar {

    private int id;
    private String tipo;
    private String nombre;
    private String rutaImagen;
    private boolean esPersonalizada;

    public PiezaAvatar() {}

    public PiezaAvatar(String tipo, String nombre, String rutaImagen) {
        this.tipo       = tipo;
        this.nombre     = nombre;
        this.rutaImagen = rutaImagen;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }

    public boolean isEsPersonalizada() { return esPersonalizada; }
    public void setEsPersonalizada(boolean esPersonalizada) { this.esPersonalizada = esPersonalizada; }

    @Override
    public String toString() {
        return "PiezaAvatar{tipo='" + tipo + "', nombre='" + nombre + "'}";
    }
}