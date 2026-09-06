import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ContactApp {

    public static class Contact {
        private String name;
        private String phone;

        public Contact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        public String getName() { return name; }
        public String getPhone() { return phone; }
    }

    private static final List<Contact> contacts = new ArrayList<>();

    static {
        contacts.add(new Contact("Alice Smith", "123-456-7890"));
        contacts.add(new Contact("Bob Jones", "987-654-3210"));
    }

    public static boolean addContact(String name, String phone) {
        if (name == null || name.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            return false;
        }
        contacts.add(new Contact(name.trim(), phone.trim()));
        return true;
    }

    public static List<Contact> searchContacts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(contacts);
        }
        List<Contact> results = new ArrayList<>();
        String q = query.trim().toLowerCase();
        for (Contact c : contacts) {
            if (c.getName().toLowerCase().contains(q) || c.getPhone().contains(q)) {
                results.add(c);
            }
        }
        return results;
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String searchQuery = "";
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    Map<String, String> params = parseFormData(br.readLine());

                    if (params.containsKey("name") && params.containsKey("phone")) {
                        addContact(params.get("name"), params.get("phone"));
                    } else if (params.containsKey("search")) {
                        searchQuery = params.get("search");
                    }
                }

                List<Contact> displayList = searchContacts(searchQuery);

                StringBuilder tableRows = new StringBuilder();
                for (Contact c : displayList) {
                    tableRows.append("<tr><td>").append(c.getName()).append("</td><td>")
                             .append(c.getPhone()).append("</td></tr>");
                }

                String html = "<html><head><style>" +
                        "body { font-family: Arial; margin: 40px; background: #f9f9f9; }" +
                        ".card { background: white; border: 1px solid #ccc; padding: 20px; border-radius: 8px; margin-bottom: 20px; max-width: 500px; }" +
                        "table { width: 100%; border-collapse: collapse; margin-top: 10px; }" +
                        "th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }" +
                        "th { background: #eee; }" +
                        "</style></head><body>" +
                        "<h2>Online Contact Book</h2>" +
                        "<div class='card'><h3>Add New Contact</h3>" +
                        "<form method='POST' action='/'>" +
                        "<input type='text' name='name' placeholder='Name' required> " +
                        "<input type='text' name='phone' placeholder='Phone Number' required> " +
                        "<button type='submit'>Add</button></form></div>" +
                        "<div class='card'><h3>Search Contacts</h3>" +
                        "<form method='POST' action='/'>" +
                        "<input type='text' name='search' placeholder='Search by name or phone'> " +
                        "<button type='submit'>Search</button> <a href='/'><button type='button'>Show All</button></a></form>" +
                        "<table><tr><th>Name</th><th>Phone</th></tr>" + tableRows + "</table></div>" +
                        "</body></html>";

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, html.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(html.getBytes());
                os.close();
            }
        });
        server.start();
        System.out.println("Contact Book running on http://localhost:8080");
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