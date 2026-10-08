package app.servlet;

import app.service.ClaudeService;
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
import java.util.Map;

public class CuriosidadesServlet extends HttpServlet {

    private static final String TEXT_HTML_UTF8 = "text/html;charset=UTF-8";
    private static final String TEMPLATES      = "/WEB-INF/templates/curiosidades/";
    private static final String SUFFIX         = ".html";

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;
    private final ObjectMapper mapper = new ObjectMapper();

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

        response.setContentType(TEXT_HTML_UTF8);
        IServletWebExchange exchange = application.buildExchange(request, response);
        WebContext context = new WebContext(exchange, request.getLocale());
        context.setVariable("username", session.getAttribute("username"));
        templateEngine.process("curiosidades", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            Map<String, String> body = mapper.readValue(request.getInputStream(),
                    mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
            String prompt = body.get("prompt");
            String respuesta = ClaudeService.preguntar(prompt);
            response.getWriter().write(mapper.writeValueAsString(Map.of("respuesta", respuesta)));
        } catch (Exception e) {
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}