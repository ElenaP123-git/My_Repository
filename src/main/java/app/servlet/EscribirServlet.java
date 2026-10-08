package app.servlet;

import app.model.Libro;
import app.repository.LibroRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EscribirServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        application = JavaxServletWebApplication.buildApplication(ctx);
        WebApplicationTemplateResolver resolver =
                new WebApplicationTemplateResolver(application);
        resolver.setPrefix("/WEB-INF/templates/escribir/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/usuario/login");
            return;
        }

        // Pasar libros del usuario al template para el selector "continuar"
        int usuarioId = (int) session.getAttribute("usuarioId");
        LibroRepo repo = new LibroRepo();
        List<Libro> libros = repo.listarBasico(usuarioId);

        // Si viene libroId en la URL, pasarlo al template para abrir directamente
        int libroIdDirecto = -1;
        String libroIdParam = request.getParameter("libroId");
        if (libroIdParam != null) {
            try { libroIdDirecto = Integer.parseInt(libroIdParam); } catch (Exception e) {}
        }

        response.setContentType("text/html;charset=UTF-8");
        IServletWebExchange exchange = application.buildExchange(request, response);
        WebContext context = new WebContext(exchange, request.getLocale());
        context.setVariable("username", session.getAttribute("username"));
        context.setVariable("libros", libros);
        context.setVariable("libroIdDirecto", libroIdDirecto);
        templateEngine.process("escribir", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.setStatus(401);
            return;
        }

        int usuarioId = (int) session.getAttribute("usuarioId");
        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "/";
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> json = new HashMap<>();
        LibroRepo repo = new LibroRepo();

        if ("/crear".equals(pathInfo)) {
            String titulo = request.getParameter("titulo");
            if (titulo == null || titulo.trim().isEmpty()) {
                json.put("ok", false);
                json.put("error", "El título no puede estar vacío.");
                response.getWriter().write(mapper.writeValueAsString(json));
                return;
            }
            titulo = titulo.trim();

            // Comprobar título duplicado
            List<Libro> existentes = repo.listarPorUsuario(usuarioId);
            for (Libro l : existentes) {
                if (l.getTitulo().equalsIgnoreCase(titulo)) {
                    json.put("ok", false);
                    json.put("error", "Este título ya existe.");
                    response.getWriter().write(mapper.writeValueAsString(json));
                    return;
                }
            }

            // Crear el libro en BD
            Libro libro = new Libro();
            libro.setUsuarioId(usuarioId);
            libro.setTitulo(titulo);
            libro.setEstado("en_proceso");
            libro.setColorPortada("#9b6dff");
            libro.setEmojiPortada("📖");
            int id = repo.insertar(libro);

            if (id > 0) {
                json.put("ok", true);
                json.put("id", id);
                json.put("titulo", titulo);
            } else {
                json.put("ok", false);
                json.put("error", "Error al guardar el libro.");
            }

        } else if ("/actualizar".equals(pathInfo)) {
            // Actualizar fecha de edición cuando el usuario guarda
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Libro libro = repo.buscarPorId(id);
                if (libro != null && libro.getUsuarioId() == usuarioId) {
                    repo.actualizar(libro);
                    json.put("ok", true);
                } else {
                    json.put("ok", false);
                }
            } catch (Exception e) {
                json.put("ok", false);
            }
        }

        response.getWriter().write(mapper.writeValueAsString(json));
    }
}