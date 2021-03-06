package server.implementation;

import server.interfaces.InterfaceShopManager;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ShopManager implements InterfaceShopManager {
    ArrayList<User> registeredUsers = new ArrayList<>();
    HashMap<UUID, UUID> allTokens = new HashMap<>();
    Market market;

    public ShopManager() {
        this.market = new Market();
        try {
            testData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registers a new user with a mail and a password.
     * @param mail mail for the new user
     * @param password password for the new user
     * @throws InstanceAlreadyExistsException throws exception if a user with this mail is already registered
     */
    @Override
    public void register(String mail, String password) throws InstanceAlreadyExistsException { //exception work?
        for (User user : registeredUsers) {
            if (user.getMail().equals(mail)) throw new InstanceAlreadyExistsException("Mail already registered!");
        }
        registeredUsers.add(new User(mail, password));
    }

    /**
     * Logs in a user with his password and mail and returns a token.
     * @param mail mail of the user
     * @param password password of the user
     * @return token which the user uses to perform further actions
     */
    @Override
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
     * @param token which user wants to log out
     */
    @Override
    public void logout(UUID token) {
        allTokens.remove(token);
    }

    /**
     * Deletes an account and makes all its products available on the market.
     * @param token token with which the account to be deleted is determined
     */
    @Override
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
    @Override
    public void buyProduct(String product, int count, UUID token) throws Exception {
        int index = getIndexOfUserByToken(token);
        User user = registeredUsers.get(index);
        if ((user.getBalance() - market.getPrice(product) * count) < 0) throw new Exception("Not enough Money!");
        user.setBalance(user.getBalance() - market.buy(product, count));
        user.updateDepot(product, count);
        registeredUsers.set(index, user);
        market.updatePrice(product);
    }

    /**
     * Sells a product from the depot adds it to the market and adjusts the balance of the user and the price of all products.
     * @param product product the user wants to sell
     * @param count amount of the product
     * @param token token of the user
     * @throws RuntimeException throws exception if the user not enough units of the product he wants to sell
     */
    @Override
    public void sellProduct(String product, int count, UUID token) {
        int index = getIndexOfUserByToken(token);
        User user = registeredUsers.get(index);
        user.updateDepot(product, -count);
        user.setBalance(user.getBalance() + market.sell(product, count));
        registeredUsers.set(index, user);
        market.updatePrice(product);
    }

    /**
     * Returns the number of times a product is offered for all products.
     * @return A Hashmap which contains the names and amounts of all products
     */
    @Override
    public HashMap<String, Integer> getAllOffers(){
        return market.getOffers();
    }

    /**
     * Returns the prices for all products.
     * @return A Hashmap which contains the names and prices of all products
     */
    @Override
    public HashMap<String, Double> getAllPrices() {
        return market.getPrices();
    }

    /**
     * Returns the depot which all items the user has.
     * @param token token of the user
     * @return depot of the user
     */
    @Override
    public HashMap<String, Integer> getDepot(UUID token) {
        return registeredUsers.get(getIndexOfUserByToken(token)).getDepot();
    }

    /**
     * Returns the amount of money the user has.
     * @param token token of the user
     * @return balance
     */
    @Override
    public double getBalance(UUID token) {
        return registeredUsers.get(getIndexOfUserByToken(token)).getBalance();
    }

    /**
     * Sets the amount of money for a user.
     * @param token token of the user
     * @param amount  amount of money the user gets
     */
    //cheat-method
    @Override
    public void setBalance(UUID token, double amount) {
        registeredUsers.get(getIndexOfUserByToken(token)).setBalance(amount);
    }

    /**
     * Adds products to the market.
     * @param product type of the product
     * @param count amount which will be added
     */
    //cheat-method
    @Override
    public void addProductToOffers(String product, int count) {
        market.sell(product, count);
    }

    public UUID getUserIDByToken(UUID token) {
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

    //Creates test data for the market and the user list
    private void testData() throws Exception {
        register("timon", "123");
        register("bonnie", "123");
        register("justus", "123");
        addProductToOffers("Kidney", 2);
        addProductToOffers("Kidney-bean", 100);
        addProductToOffers("Will to live", 5);
        addProductToOffers("sample solutions", 20);
        addProductToOffers("SI Leistungstest", 1);
        addProductToOffers("Baby Yoda", 1);
        addProductToOffers("Armin in lace bra", 100);
        addProductToOffers("Armin in padded bra", 50);
        addProductToOffers("Armin in matching set", 3);
        addProductToOffers("Armin in high heels", 7);
        addProductToOffers("Armin in red underwear", 2);
        UUID token = login("timon", "123");
        setBalance(token, 20000.0);
        buyProduct("SI Leistungstest", 1, token);
        buyProduct("Armin in lace bra", 50, token);
        buyProduct("Baby Yoda", 1, token);
        logout(token);
        token = login("bonnie", "123");
        setBalance(token, 20000.0);
        buyProduct("Armin in red underwear", 1, token);
        buyProduct("Armin in high heels", 6, token);
        buyProduct("Armin in padded bra", 3, token);
        logout(token);
        token = login("justus", "123");
        setBalance(token, 200000.69);
        buyProduct("Armin in matching set", 2, token);
        logout(token);
    }
}
