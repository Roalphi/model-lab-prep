import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NewsApp {

    public static class Article {
        private String title;
        private String category;

        public Article(String title, String category) {
            this.title = title;
            this.category = category;
        }

        public String getTitle() { return title; }
        public String getCategory() { return category; }
    }

    private static final List<Article> feed = new ArrayList<>();

    static {
        feed.add(new Article("Jenkins Pipeline 2.0 Released", "Tech"));
        feed.add(new Article("Java 21 Features Deep Dive", "Development"));
    }

    public static boolean addArticle(String title, String category) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        String cat = (category == null || category.trim().isEmpty()) ? "General" : category.trim();
        feed.add(0, new Article(title.trim(), cat)); // Insert at beginning for latest news
        return true;
    }

    public static List<Article> getFeed() {
        return new ArrayList<>(feed);
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    Map<String, String> params = parseFormData(br.readLine());

                    if (params.containsKey("title")) {
                        addArticle(params.get("title"), params.get("category"));
                    }
                }

                StringBuilder newsCards = new StringBuilder();
                for (Article a : getFeed()) {
                    newsCards.append("<div class='article'>")
                             .append("<span class='tag'>").append(a.getCategory()).append("</span>")
                             .append("<h3>").append(a.getTitle()).append("</h3>")
                             .append("</div>");
                }

                String html = "<html><head><style>" +
                        "body { font-family: Arial; margin: 40px; background: #f4f4f4; }" +
                        ".container { max-width: 600px; margin: 0 auto; }" +
                        ".card { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }" +
                        "input, button { width: 100%; padding: 10px; margin: 8px 0; box-sizing: border-box; }" +
                        "button { background: #28a745; color: white; border: none; cursor: pointer; border-radius: 4px; font-weight: bold; }" +
                        ".article { background: white; padding: 15px; border-left: 4px solid #007bff; margin-bottom: 12px; border-radius: 4px; }" +
                        ".tag { background: #e9ecef; padding: 3px 8px; font-size: 12px; border-radius: 3px; font-weight: bold; color: #495057; }" +
                        "</style></head><body><div class='container'>" +
                        "<h2>Online News Feed</h2>" +
                        "<div class='card'><h3>Publish Article</h3>" +
                        "<form method='POST' action='/'>" +
                        "<input type='text' name='title' placeholder='Article Headline' required>" +
                        "<input type='text' name='category' placeholder='Category (e.g. Tech, Sports)'>" +
                        "<button type='submit'>Publish News</button></form></div>" +
                        "<h3>Latest Headlines</h3>" + newsCards +
                        "</div></body></html>";

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, html.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(html.getBytes());
                os.close();
            }
        });
        server.start();
        System.out.println("News App running on http://localhost:8080");
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