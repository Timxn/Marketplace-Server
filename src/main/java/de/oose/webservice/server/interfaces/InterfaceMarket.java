package de.oose.webservice.server.interfaces;

import java.util.HashMap;

public interface InterfaceMarket {
    /**
     * Returns the amount of money the user gets for the sell and adjusts the availability on the market. Standard revenue is 50 per product.
     *
     * @param product product that will be added
     * @param count   amount of the product which will be added
     * @return revenue of the product times the amount
     */
    double sell(String product, int count);

    /**
     * Returns the amount of money the user has to pay and adjusts the availability on the market.
     *
     * @param product product that will be bought
     * @param count   amount of the product
     * @return price of the product times the amount
     * @throws Exception Throws exception if you want to buy more products than are available.
     */
    double buy(String product, int count) throws Exception;

    /**
     * Calculates the new price for all products. Uses the old price and divides the old price by the number of available products divided by 5.
     * If there are more than 5 products the price becomes less and vice versa. If there are 5 products, the price remains the same.
     */
    void updatePrice(String product);

    /**
     * Returns the number of times the product is offered.
     *
     * @param product of which product you want the quantity
     * @return amount of times
     */
    int getOffer(String product);

    /**
     * Returns the number of times a product is offered for all products.
     *
     * @return A Hashmap which contains the names and amounts of all products
     */
    HashMap<String, Integer> getOffers();

    /**
     * Returns the price for the product.
     *
     * @param product of which product you want the price
     * @return price for one unit of the product
     */
    double getPrice(String product);

    /**
     * Returns the prices for all products.
     *
     * @return A Hashmap which contains the names and prices of all products
     */
    HashMap<String, Double> getPrices();
}
