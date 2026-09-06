import org.junit.jupiter.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountTest {
    @Test
    void testDiscount() {
        assertEquals(80.0, DiscountApp.calculateFinalPrice(100, 20), 0.01);
        assertEquals(-1, DiscountApp.calculateFinalPrice(-100, 20));
    }
}