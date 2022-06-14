package de.oose.webservice.server.interfaces;

import javax.management.InstanceAlreadyExistsException;
import java.util.HashMap;
import java.util.UUID;

public interface InterfaceShopManager {
    /**
     * Registers a new user with a mail and a password.
     *
     * @param mail     mail for the new user
     * @param password password for the new user
     * @throws InstanceAlreadyExistsException throws exception if a user with this mail is already registered
     */
    void register(String mail, String password) throws InstanceAlreadyExistsException;

    /**
     * Logs in a user with his password and mail and returns a token.
     *
     * @param mail     mail of the user
     * @param password password of the user
     * @return token which the user uses to perform further actions
     */
    UUID login(String mail, String password);

    /**
     * Logs out the user and deletes the token.
     *
     * @param token which user wants to log out
     */
    void logout(UUID token);

    /**
     * Deletes an account and makes all its products available on the market.
     *
     * @param token token with which the account to be deleted is determined
     */
    void deleteAccount(UUID token);

    /**
     * Buys a product from the market adds it to the depot and adjusts the balance of the user and the price of all products.
     *
     * @param product product the user wants to buy
     * @param count   amount of the product
     * @param token   token of the user
     * @throws Exception throws exception if the user has to less money for this purchase or there are not enough products on the market
     */
    void buyProduct(String product, int count, UUID token) throws Exception;

    /**
     * Sells a product from the depot adds it to the market and adjusts the balance of the user and the price of all products.
     *
     * @param product product the user wants to sell
     * @param count   amount of the product
     * @param token   token of the user
     * @throws Exception throws exception if the user not enough units of the product he wants to sell
     */
    void sellProduct(String product, int count, UUID token) throws Exception;

    /**
     * Returns the number of times a product is offered for all products.
     *
     * @return A Hashmap which contains the names and amounts of all products
     */
    HashMap<String, Integer> getAllOffers();

    /**
     * Returns the prices for all products.
     *
     * @return A Hashmap which contains the names and prices of all products
     */
    HashMap<String, Double> getAllPrices();

    /**
     * Returns the depot which all items the user has.
     *
     * @param token token of the user
     * @return depot of the user
     */
    HashMap<String, Integer> getDepot(UUID token);

    /**
     * Returns the amount of money the user has.
     *
     * @param token token of the user
     * @return balance
     */
    double getBalance(UUID token);

    /**
     * Sets the amount of money for a user.
     *
     * @param token  token of the user
     * @param amount amount of money the user gets
     */
    //cheat-method
    void setBalance(UUID token, double amount);

    /**
     * Adds products to the market.
     *
     * @param product type of the product
     * @param count   amount which will be added
     */
    //cheat-method
    void addProductToOffers(String product, int count);
}
