package app.servlet;

import app.model.Usuario;
import app.repository.UsuarioRepo;
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

public class UsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEXT_HTML_UTF8 = "text/html;charset=UTF-8";
    private static final String TEMPLATES      = "/WEB-INF/templates/usuario/";
    private static final String SUFFIX         = ".html";

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        application = JavaxServletWebApplication.buildApplication(ctx);
        WebApplicationTemplateResolver resolver =
                new WebApplicationTemplateResolver(application);
        resolver.setPrefix(TEMPLATES);
        resolver.setSuffix(SUFFIX);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(TEXT_HTML_UTF8);
        IServletWebExchange exchange = application.buildExchange(request, response);
        WebContext context = new WebContext(exchange, request.getLocale());
        String path = request.getPathInfo() != null ? request.getPathInfo().trim() : "/";

        switch (path) {
            case "/":
            case "/login":
                if (request.getSession(false) != null &&
                        request.getSession().getAttribute("usuario") != null) {
                    response.sendRedirect(request.getContextPath() + "/inicio/");
                    return;
                }
                templateEngine.process("login", context, response.getWriter());
                break;
            case "/registro":
                templateEngine.process("registro", context, response.getWriter());
                break;
            case "/logout":
                HttpSession session = request.getSession(false);
                if (session != null) session.invalidate();
                response.sendRedirect(request.getContextPath() + "/usuario/login");
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/usuario/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType(TEXT_HTML_UTF8);
        IServletWebExchange exchange = application.buildExchange(request, response);
        WebContext context = new WebContext(exchange, request.getLocale());
        String path = request.getPathInfo() != null ? request.getPathInfo().trim() : "/";
        UsuarioRepo repo = new UsuarioRepo();

        switch (path) {
            case "/login": {
                String email    = request.getParameter("email");
                String password = request.getParameter("password");
                Usuario usuario = repo.buscarPorEmail(email);
                if (usuario == null) {
                    context.setVariable("error", "No existe ninguna cuenta con ese email");
                    templateEngine.process("login", context, response.getWriter());
                    return;
                }
                if (!usuario.getPasswordHash().equals(password)) {
                    context.setVariable("error", "Contrasena incorrecta");
                    templateEngine.process("login", context, response.getWriter());
                    return;
                }
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("username", usuario.getUsername());
                response.sendRedirect(request.getContextPath() + "/inicio/");
                break;
            }
            case "/registro": {
                String username  = request.getParameter("username");
                String email     = request.getParameter("email");
                String password  = request.getParameter("password");
                String password2 = request.getParameter("password2");
                if (!password.equals(password2)) {
                    context.setVariable("error", "Las contrasenas no coinciden");
                    templateEngine.process("registro", context, response.getWriter());
                    return;
                }
                if (repo.existeEmail(email)) {
                    context.setVariable("error", "Ya existe una cuenta con ese email");
                    templateEngine.process("registro", context, response.getWriter());
                    return;
                }
                if (repo.existeUsername(username)) {
                    context.setVariable("error", "Ese nombre de usuario ya esta en uso");
                    templateEngine.process("registro", context, response.getWriter());
                    return;
                }
                int nuevoId = repo.insertar(username, email, password);
                if (nuevoId == -1) {
                    context.setVariable("error", "Error al crear la cuenta");
                    templateEngine.process("registro", context, response.getWriter());
                    return;
                }
                response.sendRedirect(request.getContextPath() + "/usuario/login?registrado=true");
                break;
            }
            default:
                response.sendRedirect(request.getContextPath() + "/usuario/login");
        }
    }
}