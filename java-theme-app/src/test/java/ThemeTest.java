import org.junit.jupiter.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThemeTest {
    @Test
    void testToggle() {
        assertEquals("dark", ThemeApp.toggleTheme("light"));
        assertEquals("light", ThemeApp.toggleTheme("dark"));
    }
}