package server.implementation;

import java.util.HashMap;

public class Market {
    private final HashMap<String, Integer> offers = new HashMap<>();
    private final HashMap<String, Integer> prices = new HashMap<>();

    public int sell(String product, int count) { //gibt das geld zurück was der user für den verkauf bekommt und fügt die produkte dem markt hinzu
        if(offers.containsKey(product)) {
            offers.put(product, offers.get(product) + count);
        } else {
            offers.put(product, count);
        }
        if(!prices.containsKey(product)) {
            prices.put(product, 50); //new products get this prize
        }
        return prices.get(product) * count;
    }

    public int buy(String product, int count) throws Exception {
        if (offers.get(product) - count < 0) throw new Exception("Not enough products available!");
        if(offers.containsKey(product)) {
            offers.put(product, offers.get(product) - count);
        } else {
            offers.put(product, count);
        }
        if(!prices.containsKey(product)) {
            prices.put(product, 50); //add custom prize method //shouldn't get here
        }
        return prices.get(product) * count;
    }

    public void updatePrice() {
        prices.replaceAll((p, v) -> v / (5 / offers.get(p))); //bei mehr als 5 produkten wir der preis günsctiger und umgekehrt
    }

    public int getOffer(String product) {
        return offers.getOrDefault(product, 0);
    }

    public HashMap<String, Integer> getOffers() {
        return offers;
    }

    public int getPrice(String product) {
        return prices.getOrDefault(product, 0);
    }

    public HashMap<String, Integer> getPrices() {
        return prices;
    }
}
