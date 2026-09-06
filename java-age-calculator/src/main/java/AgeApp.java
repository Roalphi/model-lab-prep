import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.time.Year;
import java.nio.charset.StandardCharsets;

public class AgeApp {

    public static int calculateAge(int birthYear) {
        int currentYear = Year.now().getValue();
        if (birthYear <= 1900 || birthYear > currentYear) return -1;
        return currentYear - birthYear;
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", exchange -> {
            String result = "";
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String line = br.readLine();
                if (line != null && line.contains("birthYear=")) {
                    try {
                        int by = Integer.parseInt(line.split("=")[1]);
                        int age = calculateAge(by);
                        if (age >= 0) {
                            result = "<h3>Your Age is: " + age + " years old</h3>";
                        } else {
                            result = "<h3 style='color:red;'>Invalid Birth Year</h3>";
                        }
                    } catch (Exception e) {
                        result = "<h3 style='color:red;'>Invalid Input</h3>";
                    }
                }
            }

            String html = "<html><head><style>body{font-family:Arial;padding:40px;background:#f4f6f8;}" +
                    ".card{background:white;padding:25px;border-radius:8px;max-width:350px;margin:0 auto;box-shadow:0 2px 4px rgba(0,0,0,0.1);}" +
                    "input,button{width:100%;padding:10px;margin:8px 0;box-sizing:border-box;}</style></head><body>" +
                    "<div class='card'><h2>Age Calculator</h2>" + result +
                    "<form method='POST' action='/'><input type='number' name='birthYear' placeholder='Enter Birth Year (e.g. 2000)' required>" +
                    "<button type='submit'>Calculate Age</button></form></div></body></html>";

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