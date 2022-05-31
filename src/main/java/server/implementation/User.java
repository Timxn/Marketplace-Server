package server.implementation;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

public class User implements server.interfaces.User {
    private UUID userID;
    private String mail;
    private String password;
    private double balance;

    // Hashmap(productID, numberOfOwnedProducts)
    HashMap<UUID, Integer> ownedProducts = new HashMap<>();

    /**
     * Buy product. If you want to buy 2 products XXXX where each XXXX costs 42 use buy(XXXX, 2, 42)
     *
     * @param productID        the product id
     * @param numberOfProducts the number of products
     * @param price            the price
     * @throws NoSuchElementException if not enough money
     * @return the new number of the product
     */
    public int buy(UUID productID, int numberOfProducts, double price) {
        if (balance < numberOfProducts*price) throw new NoSuchElementException("Not enough money");
        ownedProducts.replace(productID, ownedProducts.get(productID)+numberOfProducts);
        balance =- (price*numberOfProducts);
        return ownedProducts.get(productID);
    }

    /**
     * Sell product. If you want to sell 2 products XXXX where each XXXX costs 42 use sell(XXXX, 2, 42)
     *
     * @param productID        the product id
     * @param numberOfProducts the number of products
     * @param price            the price
     * @throws NoSuchElementException if not enough money
     * @return the new number of the product
     */
    public int sell(UUID productID, int numberOfProducts, double price) {
        if (ownedProducts.get(productID) < numberOfProducts) throw new NoSuchElementException("Not enough of this product");
        ownedProducts.replace(productID, ownedProducts.get(productID)-numberOfProducts);
        balance =+ (price*numberOfProducts);
        return ownedProducts.get(productID);
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User(String mail, String password) {
        this.userID = UUID.randomUUID();
        this.mail = mail;
        this.password = password;
    }

    @Override
    public UUID getUserID() {
        return userID;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
