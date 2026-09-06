import org.junit.jupiter.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class NewsTest {

    @Test
    void testAddArticle() {
        boolean result = NewsApp.addArticle("Breaking Tech News", "Tech");
        assertTrue(result);
        List<NewsApp.Article> feed = NewsApp.getFeed();
        assertEquals("Breaking Tech News", feed.get(0).getTitle());
    }

    @Test
    void testEmptyTitleFails() {
        assertFalse(NewsApp.addArticle("", "Sports"));
        assertFalse(NewsApp.addArticle(null, "Tech"));
    }

    @Test
    void testDefaultCategory() {
        NewsApp.addArticle("General Announcement", "");
        List<NewsApp.Article> feed = NewsApp.getFeed();
        assertEquals("General", feed.get(0).getCategory());
    }
}