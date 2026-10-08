package app.servlet;

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

public class InicioServlet extends HttpServlet {

    private static final String TEXT_HTML_UTF8 = "text/html;charset=UTF-8";
    private static final String TEMPLATES      = "/WEB-INF/templates/inicio/";
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

        // Si no hay sesion activa, redirige al login
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/usuario/login");
            return;
        }

        response.setContentType(TEXT_HTML_UTF8);
        IServletWebExchange exchange = application.buildExchange(request, response);
        WebContext context = new WebContext(exchange, request.getLocale());

        // Pasamos el username a la plantilla
        context.setVariable("username", session.getAttribute("username"));

        templateEngine.process("inicio", context, response.getWriter());
    }
}