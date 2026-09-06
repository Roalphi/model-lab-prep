import org.junit.jupiter.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyTest {
    @Test
    void testConversion() {
        assertEquals(83.0, CurrencyApp.convert(1.0, "USD", "INR"), 0.01);
        assertEquals(-1, CurrencyApp.convert(-10, "USD", "INR"));
    }
}