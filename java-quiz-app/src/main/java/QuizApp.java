import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QuizApp {
    public static int calculateScore(String q1, String q2) {
        int score = 0;
        if ("Paris".equalsIgnoreCase(q1)) score += 50;
        if ("4".equalsIgnoreCase(q2)) score += 50;
        return score;
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response;
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    String formData = br.readLine();
                    Map<String, String> params = parseFormData(formData);
                    
                    int score = calculateScore(params.get("q1"), params.get("q2"));
                    response = "<html><body><h2>Quiz Completed!</h2>" +
                               "<h3>Your Final Score: " + score + " / 100</h3>" +
                               "<a href='/'>Try Again</a></body></html>";
                } else {
                    response = "<html><body>" +
                               "<h2>Java Interactive MCQ Quiz</h2>" +
                               "<form method='POST' action='/'>" +
                               "<p><b>1. What is the capital of France?</b><br>" +
                               "<input type='radio' name='q1' value='Paris' required> Paris<br>" +
                               "<input type='radio' name='q1' value='London'> London</p>" +
                               "<p><b>2. What is 2 + 2?</b><br>" +
                               "<input type='radio' name='q2' value='3'> 3<br>" +
                               "<input type='radio' name='q2' value='4' required> 4</p>" +
                               "<button type='submit'>Submit Quiz</button>" +
                               "</form></body></html>";
                }
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
        server.start();
        System.out.println("Quiz App running at http://localhost:8080");
    }

    private static Map<String, String> parseFormData(String formData) throws IOException {
        Map<String, String> map = new HashMap<>();
        if (formData == null || formData.isEmpty()) return map;
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                map.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }
}