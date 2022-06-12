package server.implementation;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ShopManager {
    ArrayList<User> registeredUsers = new ArrayList<>();
    HashMap<UUID, UUID> allTokens = new HashMap<>();
    Market market = new Market();

    /**
     * Registers a new user with a mail and a password.
     * @param mail mail for the new user
     * @param password password for the new user
     * @throws InstanceAlreadyExistsException throws exception if a user with this mail is already registered
     */
    public void register(String mail, String password) throws InstanceAlreadyExistsException { //exception work?
        for (User user : registeredUsers) {
            if (user.getMail().equals(mail)) throw new InstanceAlreadyExistsException("Mail already registered!");
        }
        registeredUsers.add(new User(mail, password));
    }

    /**
     * Logs in a user with his passowrt and mail and returns a token.
     * @param mail mail of the user
     * @param password password of the user
     * @return token which the user uses to perform further actions
     */
    public UUID login(String mail, String password) {
        for (User user : registeredUsers) {
            if (user.getMail().equals(mail) && user.getPassword().equals(password)) {
                UUID token = UUID.randomUUID();
                allTokens.put(token, user.getUserID());
                return token;
            }
        }
        throw new NoSuchElementException("Login failed. Mail or password incorrect!");
    }

    /**
     * Logs out the user and deletes the token.
     * @param token which user wants to logout
     */
    public void logout(UUID token) {
        allTokens.remove(token);
    }

    /**
     * Deletes an account and makes all its products available on the market.
     * @param token token with which the account to be deleted is determined
     */
    public void deleteAccount(UUID token) { //deletes a user and first sells all their products
        User user = getUserByUserID(getUserIDByToken(token));
        HashMap<String, Integer> toSell = new HashMap<>(user.getDepot());
        for (String string : toSell.keySet()) {
            if (toSell.get(string) > 0) {
                market.sell(string, toSell.get(string));
            }
        }
        registeredUsers.remove(user);
        logout(token);
    }

    /**
     * Buys a product from the market adds it to the depot and adjusts the balance of the user and the price of all products.
     * @param product product the user wants to buy
     * @param count amount of the product
     * @param token token of the user
     * @throws Exception throws exception if the user has to less money for this purchase or there are not enough products on the market
     */
    public void buyProduct(String product, int count, UUID token) throws Exception {
        UUID userID = getUserIDByToken(token);
        int index = getIndexOfUserByToken(token);
        User user = registeredUsers.get(index);
        if ((user.getBalance() - market.getPrice(product) * count) < 0) throw new Exception("Not enough Money!");
        user.setBalance(user.getBalance() - market.buy(product, count));
        user.updateDepot(product, count);
        registeredUsers.set(index, user);
        market.updatePrice();
    }

    /**
     * Sells a product from the depot adds it to the market and adjusts the balance of the user and the price of all products.
     * @param product product the user wants to sell
     * @param count amount of the product
     * @param token token of the user
     * @throws Exception throws exception if the user not enough units of the product he wants to sell
     */
    public void sellProduct(String product, int count, UUID token) throws Exception {
        UUID userID = getUserIDByToken(token);
        int index = getIndexOfUserByToken(token);
        User user = registeredUsers.get(index);
        user.updateDepot(product, -count);
        user.setBalance(user.getBalance() + market.sell(product, count));
        registeredUsers.set(index, user);
        market.updatePrice();
    }

    /**
     * Returns the number of times a product is offered for all products.
     * @return A Hashmap which contains the names and amounts of all products
     */
    public HashMap<String, Integer> getAllOffers(){
        return market.getOffers();
    }

    /**
     * Returns the prices for all products.
     * @return A Hashmap which contains the names and prices of all products
     */
    public HashMap<String, Double> getAllPrices() {
        return market.getPrices();
    }

    /**
     * Returns the depot which all items the user has.
     * @param token token of the user
     * @return depot of the user
     */
    public HashMap<String, Integer> getDepot(UUID token) {
        return registeredUsers.get(getIndexOfUserByToken(token)).getDepot();
    }

    /**
     * Returns the amount of money the user has.
     * @param token token of the user
     * @return balance
     */
    public double getBalance(UUID token) {
        return registeredUsers.get(getIndexOfUserByToken(token)).getBalance();
    }

    /**
     * Sets the amount of money for a user.
     * @param token token of the user
     * @param amount  amount of money the user gets
     */
    public void setBalance(UUID token, double amount) {
        registeredUsers.get(getIndexOfUserByToken(token)).setBalance(amount);
    }

    /**
     * Adds products to the market.
     * @param product type of the product
     * @param count amount which will be added
     */
    public void addProductToOffers(String product, int count) {
        market.sell(product, count);
    }

    private UUID getUserIDByToken(UUID token) {
        if (allTokens.get(token) == null) throw new NoSuchElementException("This user does not exist!");
        return allTokens.get(token);
    }

    private User getUserByUserID(UUID userID) {
        int i = registeredUsers.size() - 1;
        while (!(registeredUsers.get(i).getUserID().equals(userID))) {
            if (i < 0) throw new NoSuchElementException();
            i--;
        }
        return registeredUsers.get(i);
    }

    private int getIndexOfUserByToken(UUID token) {
        return registeredUsers.indexOf(getUserByUserID(getUserIDByToken(token)));
    }
}
