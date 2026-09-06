import org.junit.jupiter.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuizTest {
    @Test
    void testPerfectScore() {
        assertEquals(100, QuizApp.calculateScore("Paris", "4"));
    }

    @Test
    void testPartialScore() {
        assertEquals(50, QuizApp.calculateScore("Paris", "wrong"));
    }

    @Test
    void testZeroScore() {
        assertEquals(0, QuizApp.calculateScore("wrong", "wrong"));
    }
}