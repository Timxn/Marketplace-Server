package server.implementation;

import javax.management.InstanceAlreadyExistsException;
import java.util.*;

public class Users implements server.interfaces.Users {
    ArrayList<User> userList = new ArrayList<User>();
    HashMap<UUID,UUID> allTokens = new HashMap<>();

    /**
     * Register.
     *
     * @param mail     the mail
     * @param password the password
     * @throws InstanceAlreadyExistsException if mail has been registered
     * @implNote POST
     * @implNote creates a new account
     */
    @Override
    public void register(String mail, String password) throws InstanceAlreadyExistsException {
        for (User user:userList) {
            if (user.getMail().equals(mail)) throw new InstanceAlreadyExistsException("Mail already registered");
        }
        User newUser = new User(mail, password);
        userList.add(newUser);
    }

    /**
     * Login uuid.
     *
     * @param mail     the mail
     * @param password the password
     * @return a token which is also
     * @throws NoSuchElementException if login is incorrect
     * @implNote POST
     * @implNote checks if mail and password matches, if so create a token and safe in the Token hashMap (HashMap<UUID,UUID> (Token, UserID))
     */
    @Override
    public UUID login(String mail, String password) {
        for (User user:userList) {
            if (user.getMail().equals(mail) && user.getPassword().equals(password)) {
                UUID token = UUID.randomUUID();
                allTokens.put(token, user.getUserID());
                return token;
            }
        }
        throw new NoSuchElementException("Login is incorrect. Either mail or password do not match");
    }

    /**
     * Logout.
     *
     * @param token the token
     * @implNote POST
     * @implNote delete the token from the token list
     */
    @Override
    public void logout(UUID token) {
        allTokens.remove(token);
    }

    /**
     * extracting userID from token, return userID
     *
     * @param token the token
     * @return the userID
     * @throws NoSuchElementException if the token cannot be found
     * @implNote search in token list for the token and return the linked userID
     */
    @Override
    public UUID checkToken(UUID token) {
        if (allTokens.size() == 0) throw new NoSuchElementException();
        if (allTokens.get(token) == null) {
            throw new NoSuchElementException();
        } else return allTokens.get(token);
    }

    /**
     * Deposit money into your account.
     *
     * @param extra the money that gets deposited
     * @return the new balance
     * @implNote POST
     */
    @Override
    public double deposit(double extra, UUID token) {
        UUID userID = checkToken(token);
        extra = Math.abs(extra);
        User user = null;
        try {
            user = getUserByID(userID);
        } catch (NoSuchElementException e) {
            System.out.println("Users.deposit: user cant be found");
        }
        user.setBalance(user.getBalance() + extra);
        return user.getBalance();
    }

    /**
     * withdraw money.
     *
     * @param checkoutSum the checkout sum
     * @return the new balance
     * @implNote POST
     * @implNote just substract the money
     */
    @Override
    public double withdraw(double checkoutSum, UUID token) {
        UUID userID = checkToken(token);
        checkoutSum = Math.abs(checkoutSum);
        User user = null;
        try {
            user = getUserByID(userID);
        } catch (NoSuchElementException e) {
            System.out.println("Users.deposit: user cant be found");
        }
        user.setBalance(user.getBalance() - checkoutSum);
        return user.getBalance();
    }

    private User getUserByID(UUID userID) {
        int i = userList.size()-1;
        while (!(userList.get(i).getUserID().equals(userID))) {
            if (i<0) throw new NoSuchElementException();
            i--;
        }
        return userList.get(i);
    }
}
