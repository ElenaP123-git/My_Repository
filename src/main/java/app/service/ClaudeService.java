package app.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ClaudeService {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL   = "llama-3.3-70b-versatile";
    private static final String API_KEY = obtenerApiKey();

    public static String preguntar(String prompt) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(30000);

        String body = "{"
                + "\"model\":\"" + MODEL + "\","
                + "\"max_tokens\":1500,"
                + "\"messages\":[{\"role\":\"user\",\"content\":"
                + "\"" + escaparJson(prompt) + "\"}]"
                + "}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        InputStream is = code >= 400 ? conn.getErrorStream() : conn.getInputStream();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) sb.append(linea);
        }

        String respuesta = sb.toString();

        // Groq devuelve formato OpenAI: choices[0].message.content
        int inicio = respuesta.indexOf("\"content\":\"");
        if (inicio == -1) throw new Exception("Respuesta inesperada: " + respuesta);

        inicio += 11;
        StringBuilder texto = new StringBuilder();
        boolean escapado = false;
        for (int i = inicio; i < respuesta.length(); i++) {
            char c = respuesta.charAt(i);
            if (escapado) {
                if (c == 'n') texto.append('\n');
                else if (c == 't') texto.append('\t');
                else texto.append(c);
                escapado = false;
            } else if (c == '\\') {
                escapado = true;
            } else if (c == '"') {
                break;
            } else {
                texto.append(c);
            }
        }
        System.out.println("GROQ RESPUESTA: " + respuesta);
        return texto.toString();
    }

    private static String obtenerApiKey() {
        try {
            java.util.Properties props = new java.util.Properties();
            InputStream entrada = ClaudeService.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            if (entrada != null) {
                props.load(entrada);
                String key = props.getProperty("claude.api.key");
                if (key != null && !key.isBlank()) return key;
            }
        } catch (Exception e) {
            System.out.println("ClaudeService - Error leyendo API key: " + e.getMessage());
        }
        return "";
    }

    private static String escaparJson(String texto) {
        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}