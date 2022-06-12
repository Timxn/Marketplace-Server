package server.implementation;

import java.util.HashMap;
import java.util.UUID;

public class User {
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

    public void updateDepot(String product, int count) throws Exception {
        if (depot.getOrDefault(product, 0) - count < 0) throw new Exception("Not enough products available!");
        if(depot.containsKey(product)) {
            depot.put(product, depot.get(product) + count);
        } else {
            depot.put(product, count);
        }
    }

    public UUID getUserID() {
        return userID;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public HashMap<String, Integer> getDepot() {
        return depot;
    }
}
