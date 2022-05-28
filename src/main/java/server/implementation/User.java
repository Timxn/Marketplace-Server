package server.implementation;

import java.util.UUID;

public class User {
    private UUID userID;
    private String mail;
    private String password;
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User(String mail, String password) {
        this.userID = UUID.randomUUID();
        this.mail = mail;
        this.password = password;
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
}
