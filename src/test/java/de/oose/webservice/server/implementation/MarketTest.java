package de.oose.webservice.server.implementation;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MarketTest {
    Market market = new Market();

    @Test
    void sell() {
        assertEquals(150, market.sell("apfel", 3));
    }

    @Test
    void buy() {
        assertThrows(Exception.class, () -> market.buy("apfel" , 1));
        market.sell("apfel", 3);
        assertEquals(150, market.buy("apfel", 3));
    }

    @Test
    void getOffer() {
        market.sell("apfel", 3);
        assertEquals(3, market.getOffer("apfel"));
        assertEquals(0, market.getOffer("bapfel"));
    }

    @Test
    void getOffers() {
        market.sell("apfel", 3);
        market.sell("bapfel", 2);
        HashMap<String, Integer> offers = new HashMap<>(market.getOffers());
        assertEquals(3, offers.get("apfel"));
        assertEquals(2, offers.get("bapfel"));
    }

    @Test
    void getPrice() {
        market.sell("apfel", 3);
        assertEquals(50, market.getPrice("apfel"));
        assertEquals(0, market.getPrice("bapfel"));
    }

    @Test
    void getPrices() {
        market.sell("apfel", 3);
        market.sell("bapfel", 3);
        HashMap<String, Double> prices = new HashMap<>(market.getPrices());
        assertEquals(50, prices.get("apfel"));
        assertEquals(50, prices.get("bapfel"));
    }

    @Test
    void updatePrice() {
        market.sell("apfel", 3);
        market.sell("bapfel", 7);
        market.updatePrice("apfel");
        market.updatePrice("bapfel");
        assertEquals(83.33333333333334, market.getPrice("apfel"));
        assertEquals(35.714285714285715, market.getPrice("bapfel"));
    }
}