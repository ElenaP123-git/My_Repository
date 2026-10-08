package app.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * Servicio de generación de imágenes usando Pollinations.ai
 * - 100% gratuito, sin API key, sin registro
 * - Apto para producción y uso comercial
 * - API simple: GET https://image.pollinations.ai/prompt/{prompt}
 */
public class HuggingFaceService {

    private static final String BASE_URL = "https://image.pollinations.ai/prompt/";

    public static String generarMapa(String prompt) throws Exception {
        // Prompt sencillo: mapa de fantasía estilo dibujado a mano, fiel a la silueta
        String promptCompleto = prompt
                + ", fantasy map, simple hand-drawn style, black ink on parchment, "
                + "outline of continents and islands, top-down view, minimal detail, "
                + "clean lines, no text, no labels, no people, no buildings";

        String promptEncoded = URLEncoder.encode(promptCompleto, "UTF-8");

        // Pollinations devuelve la imagen directamente como PNG
        String urlStr = BASE_URL + promptEncoded
                + "?width=768&height=512&nologo=true&seed=" + (int)(Math.random() * 99999);

        System.out.println("Pollinations URL: " + urlStr);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(90000); // puede tardar hasta 90s la primera imagen
        conn.setInstanceFollowRedirects(true);

        int code = conn.getResponseCode();
        System.out.println("Pollinations response code: " + code);

        if (code != 200) {
            InputStream err = conn.getErrorStream();
            String errMsg = err != null ? new String(err.readAllBytes()) : "Sin respuesta";
            throw new Exception("Pollinations error " + code + ": " + errMsg);
        }

        byte[] imagenBytes = conn.getInputStream().readAllBytes();
        System.out.println("Pollinations image bytes: " + imagenBytes.length);

        if (imagenBytes.length < 1000) {
            throw new Exception("Imagen demasiado pequeña, reintenta");
        }

        String base64 = Base64.getEncoder().encodeToString(imagenBytes);
        return "data:image/png;base64," + base64;
    }
}