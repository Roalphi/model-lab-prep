import org.junit.jupiter.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testCelsiusToFahrenheit() {
        assertEquals(32.0, App.celsiusToFahrenheit(0), 0.001);
        assertEquals(212.0, App.celsiusToFahrenheit(100), 0.001);
    }

    @Test
    void testFahrenheitToCelsius() {
        assertEquals(0.0, App.fahrenheitToCelsius(32), 0.001);
        assertEquals(100.0, App.fahrenheitToCelsius(212), 0.001);
    }

    @Test
    void testTimerExecution() throws InterruptedException {
        App.TimerEngine timer = new App.TimerEngine();
        timer.start();
        assertTrue(timer.isRunning());
        Thread.sleep(1100); // Sleep ~1 second
        timer.stop();
        assertFalse(timer.isRunning());
        assertTrue(timer.getElapsedTimeSeconds() >= 1);
    }
}