package server.implementation;

import javax.management.InstanceAlreadyExistsException;
import java.util.*;

public class ShopManager implements server.interfaces.Main {
    ArrayList<UserOld> userOldList = new ArrayList<UserOld>();
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
        for (UserOld userOld : userOldList) {
            if (userOld.getMail().equals(mail)) throw new InstanceAlreadyExistsException("Mail already registered");
        }
        UserOld newUserOld = new UserOld(mail, password);
        userOldList.add(newUserOld);
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
        for (UserOld userOld : userOldList) {
            if (userOld.getMail().equals(mail) && userOld.getPassword().equals(password)) {
                UUID token = UUID.randomUUID();
                allTokens.put(token, userOld.getUserID());
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
        UserOld userOld = null;
        try {
            userOld = getUserByID(userID);
        } catch (NoSuchElementException e) {
            System.out.println("Users.deposit: user cant be found");
        }
        userOld.setBalance(userOld.getBalance() + extra);
        return userOld.getBalance();
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
        UserOld userOld = null;
        try {
            userOld = getUserByID(userID);
        } catch (NoSuchElementException e) {
            System.out.println("Users.deposit: user cant be found");
        }
        userOld.setBalance(userOld.getBalance() - checkoutSum);
        return userOld.getBalance();
    }

    private UserOld getUserByID(UUID userID) {
        int i = userOldList.size()-1;
        while (!(userOldList.get(i).getUserID().equals(userID))) {
            if (i<0) throw new NoSuchElementException();
            i--;
        }
        return userOldList.get(i);
    }
}
