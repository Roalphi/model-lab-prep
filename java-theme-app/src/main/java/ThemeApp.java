import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ThemeApp {

    public static String toggleTheme(String currentTheme) {
        return "dark".equalsIgnoreCase(currentTheme) ? "light" : "dark";
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", exchange -> {
            String theme = "light";
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String line = br.readLine();
                if (line != null && line.contains("theme=")) {
                    theme = toggleTheme(line.split("=")[1]);
                }
            }

            String bg = theme.equals("dark") ? "#222222" : "#ffffff";
            String fg = theme.equals("dark") ? "#ffffff" : "#222222";

            String html = "<html><head><style>" +
                    "body { background: " + bg + "; color: " + fg + "; font-family: Arial; text-align: center; padding-top: 50px; transition: 0.3s; }" +
                    "button { padding: 12px 24px; font-size: 16px; cursor: pointer; border-radius: 6px; border: none; background: #007bff; color: white; }" +
                    "</style></head><body>" +
                    "<h2>Current Theme: " + theme.toUpperCase() + "</h2>" +
                    "<form method='POST' action='/'><input type='hidden' name='theme' value='" + theme + "'>" +
                    "<button type='submit'>Toggle Theme</button></form>" +
                    "</body></html>";

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        });
        server.start();
        System.out.println("Running on http://localhost:8080");
    }
}