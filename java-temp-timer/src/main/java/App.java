import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class App {

    // Temperature Conversion Methods
    public static double celsiusToFahrenheit(double c) { return (c * 9 / 5) + 32; }
    public static double fahrenheitToCelsius(double f) { return (f - 32) * 5 / 9; }

    // Start/Stop Timer Engine
    public static class TimerEngine {
        private long startTime = 0;
        private long stopTime = 0;
        private boolean running = false;

        public void start() {
            this.startTime = System.currentTimeMillis();
            this.running = true;
        }

        public void stop() {
            this.stopTime = System.currentTimeMillis();
            this.running = false;
        }

        public long getElapsedTimeSeconds() {
            if (running) {
                return (System.currentTimeMillis() - startTime) / 1000;
            }
            return (stopTime - startTime) / 1000;
        }

        public boolean isRunning() { return running; }
    }

    private static final TimerEngine timer = new TimerEngine();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "";
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    Map<String, String> params = parseFormData(br.readLine());

                    if (params.containsKey("action")) {
                        String action = params.get("action");
                        if ("start".equals(action)) timer.start();
                        else if ("stop".equals(action)) timer.stop();
                    } else if (params.containsKey("temp")) {
                        try {
                            double val = Double.parseDouble(params.get("temp"));
                            String type = params.get("type");
                            if ("c2f".equals(type)) {
                                response = "<h3>Result: " + val + " °C = " + celsiusToFahrenheit(val) + " °F</h3>";
                            } else {
                                response = "<h3>Result: " + val + " °F = " + fahrenheitToCelsius(val) + " °C</h3>";
                            }
                        } catch (Exception e) {
                            response = "<h3>Error: Invalid number</h3>";
                        }
                    }
                }

                String timerStatus = timer.isRunning() ? "Running..." : "Stopped";
                long elapsed = timer.getElapsedTimeSeconds();

                String html = "<html><head><style>" +
                        "body { font-family: Arial; margin: 40px; }" +
                        ".card { border: 1px solid #ccc; padding: 20px; border-radius: 8px; margin-bottom: 20px; max-width: 400px; }" +
                        "</style></head><body>" +
                        "<h2>Java Temperature Converter & Timer</h2>" + response +
                        "<div class='card'><h3>1. Temperature Converter</h3>" +
                        "<form method='POST' action='/'>" +
                        "<input type='number' step='any' name='temp' required placeholder='Enter Value'> " +
                        "<select name='type'><option value='c2f'>Celsius to Fahrenheit</option>" +
                        "<option value='f2c'>Fahrenheit to Celsius</option></select><br><br>" +
                        "<button type='submit'>Convert</button></form></div>" +
                        "<div class='card'><h3>2. Start / Stop Timer</h3>" +
                        "<p>Status: <b>" + timerStatus + "</b> | Elapsed Time: <b>" + elapsed + "s</b></p>" +
                        "<form method='POST' action='/' style='display:inline;'><input type='hidden' name='action' value='start'><button type='submit'>Start</button></form> " +
                        "<form method='POST' action='/' style='display:inline;'><input type='hidden' name='action' value='stop'><button type='submit'>Stop</button></form> " +
                        "<a href='/'><button>Refresh Timer</button></a></div>" +
                        "</body></html>";

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, html.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(html.getBytes());
                os.close();
            }
        });
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }

    private static Map<String, String> parseFormData(String formData) throws IOException {
        Map<String, String> map = new HashMap<>();
        if (formData == null || formData.isEmpty()) return map;
        for (String pair : formData.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8), URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }
}