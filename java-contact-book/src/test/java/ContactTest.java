import org.junit.jupiter.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {

    @Test
    void testAddContact() {
        boolean result = ContactApp.addContact("Charlie Brown", "555-0199");
        assertTrue(result);
        List<ContactApp.Contact> matches = ContactApp.searchContacts("Charlie");
        assertEquals(1, matches.size());
        assertEquals("555-0199", matches.get(0).getPhone());
    }

    @Test
    void testAddEmptyContactFails() {
        assertFalse(ContactApp.addContact("", "12345"));
        assertFalse(ContactApp.addContact("John", ""));
    }

    @Test
    void testSearchContact() {
        List<ContactApp.Contact> results = ContactApp.searchContacts("Alice");
        assertFalse(results.isEmpty());
        assertEquals("Alice Smith", results.get(0).getName());
    }
}