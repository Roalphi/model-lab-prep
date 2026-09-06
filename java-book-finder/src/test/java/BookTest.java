import org.junit.jupiter.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
    @Test
    void testSearch() {
        List<BookApp.Book> res = BookApp.searchBooks("Clean Code");
        assertEquals(1, res.size());
        assertEquals("Robert C. Martin", res.get(0).getAuthor());
    }
}