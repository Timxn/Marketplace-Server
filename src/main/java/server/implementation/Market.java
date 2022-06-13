package server.implementation;

import java.util.HashMap;

public class Market implements server.interfaces.InterfaceMarket {
    private final HashMap<String, Integer> offers = new HashMap<>();
    private final HashMap<String, Double> prices = new HashMap<>();

    /**
     * Returns the amount of money the user gets for the sell and adjusts the availability on the market. Standard revenue is 50 per product.
     * @param product product that will be added
     * @param count amount of the product which will be added
     * @return revenue of the product times the amount
     */
    @Override
    public double sell(String product, int count) {
        if (offers.containsKey(product)) {
            offers.put(product, offers.get(product) + count);
        } else {
            offers.put(product, count);
        }
        if (!prices.containsKey(product)) {
            prices.put(product, 50.0);
        }
        return prices.get(product) * count;
    }

    /**
     * Returns the amount of money the user has to pay and adjusts the availability on the market.
     * @param product product that will be bought
     * @param count amount of the product
     * @return price of the product times the amount
     * @throws RuntimeException Throws exception if you want to buy more products than are available.
     */
    @Override
    public double buy(String product, int count) {
        if (offers.getOrDefault(product, 0 ) - count < 0) throw new RuntimeException("Not enough products available!");
        offers.put(product, offers.get(product) - count);
        return prices.get(product) * count;
    }

    /**
     * Calculates the new price for all products. Uses the old price and divides the old price by the number of available products divided by 5.
     * If there are more than 5 products the price becomes less and vice versa. If there are 5 products, the price remains the same.
     */
    @Override
    public void updatePrice() {
        prices.replaceAll((p, v) -> v / (offers.get(p) / 5.0));
    }

    /**
     * Returns the number of times the product is offered.
     * @param product of which product you want the quantity
     * @return amount of times
     */
    @Override
    public int getOffer(String product) {
        return offers.getOrDefault(product, 0);
    }

    /**
     * Returns the number of times a product is offered for all products.
     * @return A Hashmap which contains the names and amounts of all products
     */
    @Override
    public HashMap<String, Integer> getOffers() {
        return offers;
    }

    /**
     * Returns the price for the product.
     * @param product of which product you want the price
     * @return price for one unit of the product
     */
    @Override
    public double getPrice(String product) {
        return prices.getOrDefault(product, 0.0);
    }

    /**
     * Returns the prices for all products.
     * @return A Hashmap which contains the names and prices of all products
     */
    @Override
    public HashMap<String, Double> getPrices() {
        return prices;
    }
}
