import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TodoApp {
    static List<String> tasks = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 1. READ / HOME
        server.createContext("/", e -> {
            if (e.getRequestMethod().equals("GET")) {
                StringBuilder list = new StringBuilder();
                for (int i = 0; i < tasks.size(); i++) {
                    list.append(String.format("<li>%s <a href='/delete?index=%d'>Delete</a></li>", tasks.get(i), i));
                }
                String html = "<html><head><style>"
    + "body { font-family: sans-serif; max-width: 400px; margin: 40px auto; padding: 20px; }"
    + "input, button { padding: 8px; margin: 4px; }"
    + "li { margin: 8px 0; }"
    + "</style></head><body>"
    + "<h1>To-Do</h1>"
    + "<form method='post' action='/add'><input name='task' required/><button>Add</button></form>"
    + "<ul>" + list + "</ul>"
    + "</body></html>";
                sendResponse(e, html);
            }
        });

        // 2. CREATE / ADD
        server.createContext("/add", e -> {
            if (e.getRequestMethod().equals("POST")) {
                String task = getValue(readRequest(e), "task");
                if (task != null) tasks.add(task.trim());
                redirect(e, "/");
            }
        });

        // 3. DELETE
        server.createContext("/delete", e -> {
            if (e.getRequestMethod().equals("GET")) {
                int idx = Integer.parseInt(getValue(e.getRequestURI().getQuery(), "index"));
                if (idx >= 0 && idx < tasks.size()) tasks.remove(idx);
                redirect(e, "/");
            }
        });

        server.start();
        System.out.println("Running on 8080...");
    }

    // --- 4 UTILITY HELPERS ---
    static void sendResponse(HttpExchange e, String res) throws IOException {
        byte[] b = res.getBytes(StandardCharsets.UTF_8);
        e.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        e.sendResponseHeaders(200, b.length);
        e.getResponseBody().write(b);
        e.close();
    }

    static void redirect(HttpExchange e, String loc) throws IOException {
        e.getResponseHeaders().set("Location", loc);
        e.sendResponseHeaders(302, -1);
        e.close();
    }

    static String readRequest(HttpExchange e) throws IOException {
        return new String(e.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    static String getValue(String data, String key) {
        if (data == null) return null;
        for (String pair : data.split("&")) {
            String[] p = pair.split("=", 2);
            if (p.length == 2 && p[0].equals(key)) return URLDecoder.decode(p[1], StandardCharsets.UTF_8);
        }
        return null;
    }
}