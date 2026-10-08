package app.servlet;

import app.service.HuggingFaceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * POST /mapa/generar
 * Body JSON: { "prompt": "...", "genero": "Fantasía", "nombreRegion": "Continente Norte" }
 * Response JSON: { "imagen": "data:image/jpeg;base64,..." }
 *            o   { "error": "...", "reintenta": true } si el modelo está cargando
 */
public class MapaServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Requiere sesión activa
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"No autenticado\"}");
            return;
        }

        response.setContentType("application/json;charset=UTF-8");

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = mapper.readValue(request.getInputStream(), Map.class);
            String genero       = body.containsKey("genero")       ? body.get("genero").toString()       : "Fantasía";
            String nombreRegion = body.containsKey("nombreRegion") ? body.get("nombreRegion").toString() : "continente";
            String formas       = body.containsKey("formas")       ? body.get("formas").toString()       : "varias masas de tierra irregulares";

            // Construir prompt adaptado al género
            String prompt = construirPrompt(genero, nombreRegion, formas);

            String imagenBase64 = HuggingFaceService.generarMapa(prompt);
            String json = mapper.writeValueAsString(Map.of("imagen", imagenBase64));
            response.getWriter().write(json);

        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "Error desconocido";
            if ("MODEL_LOADING".equals(msg)) {
                // El modelo de HuggingFace está arrancando (cold start), pedir reintento
                response.getWriter().write("{\"error\":\"El modelo está cargando, espera unos segundos\",\"reintenta\":true}");
            } else {
                System.err.println("MapaServlet error: " + msg);
                response.getWriter().write("{\"error\":\"" + escaparJson(msg) + "\"}");
            }
        }
    }

    private String construirPrompt(String genero, String nombreRegion, String formas) {
        String base = "fantasy world map region called " + nombreRegion
                + ", " + formas + ", ";

        switch (genero) {
            case "Fantasía":
                return base + "high fantasy style, glowing magical lands, ancient ruins, "
                        + "mystical forests, dragon territories marked, rich greens and purples";
            case "Terror":
                return base + "dark gothic map, cursed lands, swamps, dead forests, "
                        + "shadowy regions, dark parchment, blood red accents, ominous";
            case "Ciencia ficción":
                return base + "sci-fi planet map, alien terrain, technological outposts, "
                        + "grid overlays, blue and cyan tones, futuristic cartography";
            case "Romance":
                return base + "romantic fantasy map, rolling hills, gardens, castles, "
                        + "warm golden tones, soft watercolor style, idyllic landscapes";
            case "Misterio":
                return base + "mysterious map, hidden paths, secret locations, fog effects, "
                        + "dark purple tones, cryptic symbols, old ink style";
            case "Aventura":
                return base + "adventure map, mountains, jungles, treasure locations, "
                        + "bold colors, classic fantasy RPG style, dramatic terrain";
            case "Histórico":
                return base + "historical map style, realistic terrain, ancient cartography, "
                        + "sepia and brown tones, accurate topography, period accurate";
            case "Cozy":
                return base + "cozy fantasy map, peaceful villages, gentle hills, "
                        + "warm amber tones, soft illustration style, welcoming landscapes";
            default:
                return base + "detailed fantasy map, varied terrain, parchment background";
        }
    }

    private String escaparJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "");
    }
}