package de.oose.webservice.server.implementation;

import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShopManagerTest {
    ShopManager shopManager = new ShopManager();

    @Test
    void register() throws InstanceAlreadyExistsException {
        shopManager.register("123", "123");
        assertThrows(InstanceAlreadyExistsException.class, () -> shopManager.register("123", "123"));
    }

    @Test
    void login() throws InstanceAlreadyExistsException {
        shopManager.register("123", "123");
        shopManager.login("123", "123");
        assertThrows(NoSuchElementException.class, () -> shopManager.login("13", "123"));
    }

    @Test
    void logout() throws InstanceAlreadyExistsException {
        shopManager.register("123", "123");
        UUID token = shopManager.login("123", "123");
        shopManager.logout(token);
    }

    @Test
    void buyProduct() throws Exception {
        shopManager.register("123", "123");
        UUID token = shopManager.login("123", "123");
        shopManager.addProductToOffers("apfel", 4);
        shopManager.buyProduct("apfel", 2, token);
        assertEquals(0.0, shopManager.getBalance(token));
        assertEquals(2, shopManager.getDepot(token).get("apfel"));
        assertEquals(2, shopManager.getAllOffers().get("apfel"));
    }

    @Test
    void sellProduct() throws Exception {
        shopManager.register("123", "123");
        UUID token = shopManager.login("123", "123");
        shopManager.addProductToOffers("apfel", 4);
        shopManager.buyProduct("apfel", 2, token);
        shopManager.sellProduct("apfel", 1, token);
        assertEquals(125.0, shopManager.getBalance(token));
        assertEquals(1, shopManager.getDepot(token).get("apfel"));
        assertEquals(3, shopManager.getAllOffers().get("apfel"));
    }

    @Test
    void getAllOffers() {
        shopManager.addProductToOffers("apfel", 4);
        assertEquals(4, shopManager.getAllOffers().get("apfel"));
    }

    @Test
    void getAllPrices() {
        shopManager.addProductToOffers("apfel", 4);
        assertEquals(50.0, shopManager.getAllPrices().get("apfel"));
    }

    @Test
    void getDepot() throws Exception {
        shopManager.register("123", "123");
        UUID token = shopManager.login("123", "123");
        shopManager.addProductToOffers("apfel", 4);
        shopManager.buyProduct("apfel", 2, token);
        assertEquals(2, shopManager.getDepot(token).get("apfel"));
    }

    @Test
    void getBalance() throws Exception {
        shopManager.register("123", "123");
        UUID token = shopManager.login("123", "123");
        shopManager.addProductToOffers("apfel", 4);
        shopManager.buyProduct("apfel", 1, token);
        assertEquals(50.0, shopManager.getBalance(token));
    }

    @Test
    void setBalance() throws InstanceAlreadyExistsException {
        shopManager.register("123", "123");
        UUID token = shopManager.login("123", "123");
        shopManager.setBalance(token, 3000.0);
        assertEquals(3000.0, shopManager.getBalance(token));
    }

    @Test
    void deleteAccount() throws Exception {
        shopManager.register("123", "123");
        UUID token = shopManager.login("123", "123");
        shopManager.addProductToOffers("apfel", 4);
        shopManager.buyProduct("apfel", 2, token);
        assertEquals(2, shopManager.getAllOffers().get("apfel"));
        shopManager.deleteAccount(token);
        assertEquals(4, shopManager.getAllOffers().get("apfel"));
    }
}