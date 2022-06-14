package de.oose.webservice.server.interfaces;

import java.util.HashMap;
import java.util.UUID;

public interface InterfaceUser {
    /**
     * Adjusts the number of products in the depot.
     *
     * @param product product that will be changed
     * @param count   the number to be changed (positive or negative)
     * @throws Exception throws exception if less than 0 products would be available after deduction
     */
    void updateDepot(String product, int count) throws Exception;

    /**
     * Returns the userID of the user.
     *
     * @return userID
     */
    UUID getUserID();

    /**
     * Returns the mail of the user.
     *
     * @return mail
     */
    String getMail();

    /**
     * Returns the password of the user.
     *
     * @return password
     */
    String getPassword();

    /**
     * Returns the amount of money the user has.
     *
     * @return balance
     */
    double getBalance();

    /**
     * Alters the amount of money the user has.
     *
     * @param balance the new amount of money
     */
    void setBalance(double balance);

    /**
     * Returns the depot which all items the user has.
     *
     * @return depot of the user
     */
    HashMap<String, Integer> getDepot();
}
