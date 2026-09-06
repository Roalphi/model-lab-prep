import org.junit.jupiter.Test;
import java.time.Year;
import static org.junit.jupiter.api.Assertions.*;

public class AgeTest {
    @Test
    void testAgeCalculation() {
        int current = Year.now().getValue();
        assertEquals(current - 2000, AgeApp.calculateAge(2000));
        assertEquals(-1, AgeApp.calculateAge(1800));
        assertEquals(-1, AgeApp.calculateAge(3000));
    }
}