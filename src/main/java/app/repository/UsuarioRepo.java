package app.repository;

import app.config.ConectorMySQL;
import app.exceptions.MyException;
import app.model.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UsuarioRepo {

    private ConectorMySQL conector;

    public UsuarioRepo() {
        try {
            this.conector = new ConectorMySQL();
        } catch (MyException e) {
            System.out.println("UsuarioRepo - Error al conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int contarUsuarios() {
        return contarFila("SELECT COUNT(*) FROM usuarios");
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT id, username, email, password_hash, fecha_registro "
                + "FROM usuarios WHERE email = ?";
        List<Map<String, String>> filas = listarConParametro(sql,
                new String[]{"id","username","email","password_hash","fecha_registro"},
                email);
        if (filas.isEmpty()) return null;
        return mapearUsuario(filas.get(0));
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT id, username, email, password_hash, fecha_registro "
                + "FROM usuarios WHERE id = ?";
        List<Map<String, String>> filas = listarConParametro(sql,
                new String[]{"id","username","email","password_hash","fecha_registro"},
                String.valueOf(id));
        if (filas.isEmpty()) return null;
        return mapearUsuario(filas.get(0));
    }

    public boolean existeEmail(String email) {
        return contarFilaConParametro(
                "SELECT COUNT(*) FROM usuarios WHERE email = ?", email) > 0;
    }

    public boolean existeUsername(String username) {
        return contarFilaConParametro(
                "SELECT COUNT(*) FROM usuarios WHERE username = ?", username) > 0;
    }

    public List<Map<String, String>> listarTodos() {
        return listar(
                "SELECT id, username, email, fecha_registro FROM usuarios ORDER BY fecha_registro DESC",
                new String[]{"id","username","email","fecha_registro"}
        );
    }

    public int insertar(String username, String email, String passwordHash) {
        String sql = "INSERT INTO usuarios (username, email, password_hash) VALUES (?, ?, ?)";
        return insertarFila(sql, username, email, passwordHash);
    }

    private int contarFila(String sql) {
        try {
            Statement stmt = conector.getConexion().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("UsuarioRepo contarFila - " + e.getMessage());
        }
        return 0;
    }

    private int contarFilaConParametro(String sql, String parametro) {
        try {
            PreparedStatement ps = conector.getConexion().prepareStatement(sql);
            ps.setString(1, parametro);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("UsuarioRepo contarFilaConParametro - " + e.getMessage());
        }
        return 0;
    }

    private List<Map<String, String>> listar(String sql, String[] columnas) {
        List<Map<String, String>> filas = new ArrayList<>();
        try {
            Statement stmt = conector.getConexion().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, String> fila = new LinkedHashMap<>();
                for (String col : columnas) {
                    String val = rs.getString(col);
                    fila.put(col, val != null ? val : "-");
                }
                filas.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("UsuarioRepo listar - " + e.getMessage());
        }
        return filas;
    }

    private List<Map<String, String>> listarConParametro(String sql, String[] columnas, String parametro) {
        List<Map<String, String>> filas = new ArrayList<>();
        try {
            PreparedStatement ps = conector.getConexion().prepareStatement(sql);
            ps.setString(1, parametro);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> fila = new LinkedHashMap<>();
                for (String col : columnas) {
                    String val = rs.getString(col);
                    fila.put(col, val != null ? val : "-");
                }
                filas.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("UsuarioRepo listarConParametro - " + e.getMessage());
        }
        return filas;
    }

    private int insertarFila(String sql, String... parametros) {
        try {
            PreparedStatement ps = conector.getConexion()
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < parametros.length; i++) {
                ps.setString(i + 1, parametros[i]);
            }
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.out.println("UsuarioRepo insertarFila - " + e.getMessage());
        }
        return -1;
    }

    private Usuario mapearUsuario(Map<String, String> fila) {
        Usuario u = new Usuario();
        u.setId(Integer.parseInt(fila.get("id")));
        u.setUsername(fila.get("username"));
        u.setEmail(fila.get("email"));
        u.setPasswordHash(fila.get("password_hash"));
        String fecha = fila.get("fecha_registro");
        if (fecha != null && !fecha.equals("-")) {
            u.setFechaRegistro(LocalDateTime.parse(fecha.replace(" ", "T")));
        }
        return u;
    }
}