package app.repository;

import app.config.ConectorMySQL;
import app.exceptions.MyException;
import app.model.Libro;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LibroRepo {

    private ConectorMySQL conector;

    public LibroRepo() {
        try {
            this.conector = new ConectorMySQL();
        } catch (MyException e) {
            System.out.println("LibroRepo - Error al conectar: " + e.getMessage());
        }
    }

    // ── Listar todos los libros de un usuario ──
    public List<Libro> listarPorUsuario(int usuarioId) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE usuario_id = ? ORDER BY fecha_edicion DESC";
        try {
            PreparedStatement ps = conector.getConexion().prepareStatement(sql);
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                libros.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("LibroRepo listarPorUsuario - " + e.getMessage());
        }
        return libros;
    }

    // ── Buscar por id ──
    public Libro buscarPorId(int id) {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try {
            PreparedStatement ps = conector.getConexion().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("LibroRepo buscarPorId - " + e.getMessage());
        }
        return null;
    }

    // ── Insertar nuevo libro ──
    public int insertar(Libro libro) {
        String sql = "INSERT INTO libros (usuario_id, titulo, genero, descripcion, estado, color_portada, emoji_portada) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conector.getConexion()
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, libro.getUsuarioId());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getGenero());
            ps.setString(4, libro.getDescripcion());
            ps.setString(5, libro.getEstado() != null ? libro.getEstado() : "borrador");
            ps.setString(6, libro.getColorPortada() != null ? libro.getColorPortada() : "#9b6dff");
            ps.setString(7, libro.getEmojiPortada() != null ? libro.getEmojiPortada() : "📖");
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.out.println("LibroRepo insertar - " + e.getMessage());
        }
        return -1;
    }

    // ── Actualizar libro ──
    public void actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo=?, genero=?, descripcion=?, estado=?, "
                + "color_portada=?, emoji_portada=? WHERE id=? AND usuario_id=?";
        try {
            PreparedStatement ps = conector.getConexion().prepareStatement(sql);
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getGenero());
            ps.setString(3, libro.getDescripcion());
            ps.setString(4, libro.getEstado());
            ps.setString(5, libro.getColorPortada());
            ps.setString(6, libro.getEmojiPortada());
            ps.setInt(7, libro.getId());
            ps.setInt(8, libro.getUsuarioId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("LibroRepo actualizar - " + e.getMessage());
        }
    }

    // ── Borrar libro ──
    public void borrar(int id, int usuarioId) {
        String sql = "DELETE FROM libros WHERE id = ? AND usuario_id = ?";
        try {
            PreparedStatement ps = conector.getConexion().prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("LibroRepo borrar - " + e.getMessage());
        }
    }

    // ── Listar libros básicos (id + titulo) para el selector ──
    public List<Libro> listarBasico(int usuarioId) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT id, titulo, emoji_portada, color_portada FROM libros WHERE usuario_id = ? ORDER BY titulo";
        try {
            PreparedStatement ps = conector.getConexion().prepareStatement(sql);
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Libro l = new Libro();
                l.setId(rs.getInt("id"));
                l.setTitulo(rs.getString("titulo"));
                l.setEmojiPortada(rs.getString("emoji_portada"));
                l.setColorPortada(rs.getString("color_portada"));
                libros.add(l);
            }
        } catch (SQLException e) {
            System.out.println("LibroRepo listarBasico - " + e.getMessage());
        }
        return libros;
    }

    private Libro mapear(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setId(rs.getInt("id"));
        l.setUsuarioId(rs.getInt("usuario_id"));
        l.setTitulo(rs.getString("titulo"));
        l.setGenero(rs.getString("genero"));
        l.setDescripcion(rs.getString("descripcion"));
        l.setEstado(rs.getString("estado"));
        l.setColorPortada(rs.getString("color_portada"));
        l.setEmojiPortada(rs.getString("emoji_portada"));
        Timestamp fc = rs.getTimestamp("fecha_creacion");
        if (fc != null) l.setFechaCreacion(fc.toLocalDateTime());
        Timestamp fe = rs.getTimestamp("fecha_edicion");
        if (fe != null) {
            l.setFechaEdicion(fe.toLocalDateTime());
            l.setFechaEdicionStr(fe.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        return l;
    }
}