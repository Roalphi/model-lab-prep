import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CurrencyApp {

    public static double convert(double amount, String from, String to) {
        if (amount < 0) return -1;
        // Rates relative to USD
        Map<String, Double> rates = Map.of("USD", 1.0, "EUR", 0.92, "INR", 83.0, "GBP", 0.79);
        if (!rates.containsKey(from) || !rates.containsKey(to)) return -1;
        
        double inUSD = amount / rates.get(from);
        return Math.round(inUSD * rates.get(to) * 100.0) / 100.0;
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", exchange -> {
            String resultText = "";
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                Map<String, String> params = parseFormData(br.readLine());
                
                try {
                    double amt = Double.parseDouble(params.get("amount"));
                    String from = params.get("from");
                    String to = params.get("to");
                    double res = convert(amt, from, to);
                    resultText = "<h3>" + amt + " " + from + " = " + res + " " + to + "</h3>";
                } catch (Exception e) {
                    resultText = "<h3>Invalid Input</h3>";
                }
            }

            String html = "<html><head><style>body{font-family:Arial;padding:40px;background:#f4f6f8;}" +
                    ".card{background:white;padding:20px;border-radius:8px;max-width:400px;margin:0 auto;box-shadow:0 2px 4px rgba(0,0,0,0.1);}" +
                    "input,select,button{width:100%;padding:10px;margin:8px 0;}</style></head><body>" +
                    "<div class='card'><h2>Currency Converter</h2>" + resultText +
                    "<form method='POST' action='/'><input type='number' step='any' name='amount' placeholder='Amount' required>" +
                    "<select name='from'><option>USD</option><option>EUR</option><option>INR</option><option>GBP</option></select>" +
                    "<select name='to'><option>INR</option><option>USD</option><option>EUR</option><option>GBP</option></select>" +
                    "<button type='submit'>Convert</button></form></div></body></html>";

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        });
        server.start();
        System.out.println("Running on http://localhost:8080");
    }

    private static Map<String, String> parseFormData(String formData) throws IOException {
        Map<String, String> map = new HashMap<>();
        if (formData == null || formData.isEmpty()) return map;
        for (String pair : formData.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) map.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8), URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
        }
        return map;
    }
}