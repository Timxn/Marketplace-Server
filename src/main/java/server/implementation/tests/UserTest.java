package server.implementation.tests;

import org.junit.jupiter.api.Test;
import server.implementation.User;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user = new User("timon", "123");

    @Test
    void updateDepot() {
    }

    @Test
    void getMail() {
        assertEquals("timon", user.getMail());
    }

    @Test
    void getPassword() {
        assertEquals("123", user.getPassword());
    }

    @Test
    void getBalance() {
        assertEquals(100, user.getBalance());
    }

    @Test
    void setBalance() {
        user.setBalance(20);
        assertEquals(20, user.getBalance());
    }

    @Test
    void getDepot() throws Exception {
        user.updateDepot("apfel", 2);
        user.updateDepot("bapfel", 2);
        user.updateDepot("apfel", -1);
        assertEquals(1, user.getDepot().get("apfel"));
        assertEquals(2, user.getDepot().get("bapfel"));
        user.updateDepot("bapfel", -2);
        assertEquals(0, user.getDepot().get("bapfel"));
        assertThrows(Exception.class, () -> user.updateDepot("apfel", -2));
    }
}