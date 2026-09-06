import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DiscountApp {

    public static double calculateFinalPrice(double price, double discountPercent) {
        if (price <= 0 || discountPercent < 0 || discountPercent > 100) return -1;
        double savings = price * (discountPercent / 100.0);
        return Math.round((price - savings) * 100.0) / 100.0;
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", exchange -> {
            String resultText = "";
            int discountPct = 0;
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                Map<String, String> params = parseFormData(br.readLine());
                try {
                    double p = Double.parseDouble(params.get("price"));
                    double d = Double.parseDouble(params.get("discount"));
                    discountPct = (int) d;
                    double finalP = calculateFinalPrice(p, d);
                    if (finalP >= 0) {
                        resultText = "<h3>Final Price: $" + finalP + "</h3>";
                    } else {
                        resultText = "<h3 style='color:red;'>Invalid Price/Discount</h3>";
                    }
                } catch (Exception e) {
                    resultText = "<h3 style='color:red;'>Invalid Input</h3>";
                }
            }

            String html = "<html><head><style>body{font-family:Arial;padding:40px;background:#f4f6f8;}" +
                    ".card{background:white;padding:25px;border-radius:8px;max-width:350px;margin:0 auto;box-shadow:0 2px 4px rgba(0,0,0,0.1);}" +
                    "input,button{width:100%;padding:10px;margin:8px 0;box-sizing:border-box;}" +
                    ".progress-bg{background:#e9ecef;border-radius:4px;height:20px;width:100%;margin-top:10px;}" +
                    ".progress-bar{background:#28a745;height:100%;border-radius:4px;width:" + discountPct + "%;transition:0.3s;}</style></head><body>" +
                    "<div class='card'><h2>Discount Calculator</h2>" + resultText +
                    "<form method='POST' action='/'><input type='number' step='any' name='price' placeholder='Original Price ($)' required>" +
                    "<input type='number' step='any' name='discount' placeholder='Discount (%)' required>" +
                    "<button type='submit'>Calculate</button></form>" +
                    "<div class='progress-bg'><div class='progress-bar'></div></div>" +
                    "<p style='text-align:center;font-size:12px;'>Discount Applied: " + discountPct + "%</p>" +
                    "</div></body></html>";

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