package app.servlet;

import app.model.Libro;
import app.repository.LibroRepo;
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
import java.util.List;

public class MisLibrosServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        application = JavaxServletWebApplication.buildApplication(ctx);
        WebApplicationTemplateResolver resolver =
                new WebApplicationTemplateResolver(application);
        resolver.setPrefix("/WEB-INF/templates/mislibros/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").replace("\r", "");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/usuario/login");
            return;
        }

        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "/";

        if ("/json".equals(pathInfo)) {
            int usuarioId = (int) session.getAttribute("usuarioId");
            LibroRepo repo = new LibroRepo();
            List<Libro> libros = repo.listarPorUsuario(usuarioId);

            StringBuilder sb = new StringBuilder();
            sb.append("{\"username\":\"")
                    .append(esc(String.valueOf(session.getAttribute("username"))))
                    .append("\",\"libros\":[");

            for (int i = 0; i < libros.size(); i++) {
                Libro l = libros.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                        .append("\"id\":").append(l.getId()).append(",")
                        .append("\"titulo\":\"").append(esc(l.getTitulo())).append("\",")
                        .append("\"genero\":\"").append(esc(l.getGenero())).append("\",")
                        .append("\"descripcion\":\"").append(esc(l.getDescripcion())).append("\",")
                        .append("\"estado\":\"").append(esc(l.getEstado() != null ? l.getEstado() : "borrador")).append("\",")
                        .append("\"colorPortada\":\"").append(esc(l.getColorPortada() != null ? l.getColorPortada() : "#9b6dff")).append("\",")
                        .append("\"emojiPortada\":\"").append(esc(l.getEmojiPortada() != null ? l.getEmojiPortada() : "📖")).append("\",")
                        .append("\"fechaEdicionStr\":\"").append(esc(l.getFechaEdicionStr() != null ? l.getFechaEdicionStr() : "Recién creado")).append("\"")
                        .append("}");
            }
            sb.append("]}");

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(sb.toString());
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        IServletWebExchange exchange = application.buildExchange(request, response);
        WebContext context = new WebContext(exchange, request.getLocale());
        context.setVariable("username", session.getAttribute("username"));
        templateEngine.process("mislibros", context, response.getWriter());
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
        String pathInfo = request.getPathInfo() != null ? request.getPathInfo().trim() : "/";
        LibroRepo repo = new LibroRepo();

        switch (pathInfo) {
            case "/crear": {
                Libro libro = new Libro();
                libro.setUsuarioId(usuarioId);
                libro.setTitulo(request.getParameter("titulo"));
                libro.setGenero(request.getParameter("genero"));
                libro.setDescripcion(request.getParameter("descripcion"));
                libro.setColorPortada(request.getParameter("colorPortada"));
                libro.setEmojiPortada(request.getParameter("emojiPortada"));
                libro.setEstado("borrador");
                repo.insertar(libro);
                break;
            }
            case "/editar": {
                int id = Integer.parseInt(request.getParameter("id"));
                Libro libro = repo.buscarPorId(id);
                if (libro != null && libro.getUsuarioId() == usuarioId) {
                    libro.setTitulo(request.getParameter("titulo"));
                    libro.setGenero(request.getParameter("genero"));
                    libro.setEstado(request.getParameter("estado"));
                    repo.actualizar(libro);
                }
                break;
            }
            case "/borrar": {
                int id = Integer.parseInt(request.getParameter("id"));
                repo.borrar(id, usuarioId);
                break;
            }
        }

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"ok\":true}");
    }
}