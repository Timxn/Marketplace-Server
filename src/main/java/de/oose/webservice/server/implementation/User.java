package de.oose.webservice.server.implementation;

import de.oose.webservice.server.interfaces.InterfaceUser;

import java.util.HashMap;
import java.util.UUID;

public class User implements InterfaceUser {
    private final UUID userID;
    private final String mail;
    private final String password;
    private double balance;
    private final HashMap<String, Integer> depot = new HashMap<>();

    public User(String mail, String password) {
        this.userID = UUID.randomUUID();
        this.mail = mail;
        this.password = password;
        this.balance = 100;
    }

    /**
     * Adjusts the number of products in the depot.
     * @param product product that will be changed
     * @param count the number to be changed (positive or negative)
     * @throws RuntimeException throws exception if less than 0 products would be available after deduction
     */
    @Override
    public void updateDepot(String product, int count) {
        if (depot.getOrDefault(product, 0) + count < 0) throw new RuntimeException("Not enough products available!");
        if(depot.containsKey(product)) {
            depot.put(product, depot.get(product) + count);
        } else {
            depot.put(product, count);
        }
    }

    /**
     * Returns the userID of the user.
     * @return userID
     */
    @Override
    public UUID getUserID() {
        return userID;
    }

    /**
     * Returns the mail of the user.
     * @return mail
     */
    @Override
    public String getMail() {
        return mail;
    }

    /**
     * Returns the password of the user.
     * @return password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the amount of money the user has.
     * @return balance
     */
    @Override
    public double getBalance() {
        return balance;
    }

    /**
     * Alters the amount of money the user has.
     * @param balance the new amount of money
     */
    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Returns the depot which all items the user has.
     * @return depot of the user
     */
    @Override
    public HashMap<String, Integer> getDepot() {
        return depot;
    }
}
