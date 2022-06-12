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

    public void register(String mail, String password) throws InstanceAlreadyExistsException { //exception work?
        for (User user : registeredUsers) {
            if (user.getMail().equals(mail)) throw new InstanceAlreadyExistsException("Mail already registered!");
        }
        registeredUsers.add(new User(mail, password));
    }

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

    public void logout(UUID token) {
        allTokens.remove(token);
    }

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

    public void sellProduct(String product, int count, UUID token) throws Exception {
        UUID userID = getUserIDByToken(token);
        int index = getIndexOfUserByToken(token);
        User user = registeredUsers.get(index);
        user.updateDepot(product, -count);
        user.setBalance(user.getBalance() + market.sell(product, count));
        registeredUsers.set(index, user);
        market.updatePrice();
    }

    public HashMap<String, Integer> getAllOffers(){
        return market.getOffers();
    }

    public HashMap<String, Double> getAllPrices() {
        return market.getPrices();
    }

    public HashMap<String, Integer> getDepot(UUID token) {
        return registeredUsers.get(getIndexOfUserByToken(token)).getDepot();
    }

    public double getBalance(UUID token) {
        return registeredUsers.get(getIndexOfUserByToken(token)).getBalance();
    }

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
