import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BookApp {

    public static class Book {
        private String title;
        private String author;
        public Book(String title, String author) { this.title = title; this.author = author; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
    }

    private static final List<Book> booksDb = List.of(
        new Book("Effective Java", "Joshua Bloch"),
        new Book("Clean Code", "Robert C. Martin"),
        new Book("Java: The Complete Reference", "Herbert Schildt")
    );

    public static List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) return booksDb;
        String q = query.trim().toLowerCase();
        List<Book> res = new ArrayList<>();
        for (Book b : booksDb) {
            if (b.getTitle().toLowerCase().contains(q) || b.getAuthor().toLowerCase().contains(q)) {
                res.add(b);
            }
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", exchange -> {
            String query = "";
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                Map<String, String> params = parseFormData(br.readLine());
                if (params.containsKey("query")) query = params.get("query");
            }

            StringBuilder bookCards = new StringBuilder();
            for (Book b : searchBooks(query)) {
                bookCards.append("<div style='background:#f0f0f0;padding:10px;margin-top:10px;border-radius:4px;'>")
                         .append("<h4>").append(b.getTitle()).append("</h4>")
                         .append("<p>Author: ").append(b.getAuthor()).append("</p></div>");
            }

            String html = "<html><head><style>body{font-family:Arial;padding:40px;background:#f4f6f8;}" +
                    ".card{background:white;padding:25px;border-radius:8px;max-width:500px;margin:0 auto;}" +
                    "input,button{width:100%;padding:10px;margin:8px 0;}</style></head><body>" +
                    "<div class='card'><h2>Google Books Finder</h2>" +
                    "<form method='POST' action='/'><input type='text' name='query' placeholder='Search title or author...'>" +
                    "<button type='submit'>Search Books</button></form>" + bookCards + "</div></body></html>";

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