package server.interfaces;

import javax.management.InstanceAlreadyExistsException;
import java.util.UUID;

/**
 * The interface Users.
 */
public interface Users {
    /**
     * Register.
     * @implNote POST
     * @implNote creates a new account
     * @throws javax.management.InstanceAlreadyExistsException if mail has been registered
     * @param mail     the mail
     * @param password the password
     */
    void register(String mail, String password) throws InstanceAlreadyExistsException;

    /**
     * Login uuid.
     *
     * @implNote POST
     * @implNote checks if mail and password matches, if so create a token and safe in the Token hashMap (HashMap<UUID,UUID> (Token, UserID))
     * @throws java.util.NoSuchElementException if login is incorrect
     * @param mail     the mail
     * @param password the password
     * @return a token which is also
     */
    UUID login(String mail, String password);

    /**
     * Logout.
     *
     * @implNote POST
     * @implNote delete the token from the token list
     * @param token the token
     */
    void logout(UUID token);

    /**
     * extracting userID from token, return userID
     *
     * @implNote search in token list for the token and return the linked userID
     * @param token the token
     * @throws java.util.NoSuchElementException if the token cannot be found
     * @return the userID
     */
    UUID checkToken(UUID token);

    /**
     * Deposit money into your account.
     *
     * @implNote POST
     * @param extra the money that gets deposited
     * @return the new balance
     */
    double deposit(double extra);

    /**
     * Checkout money.
     *
     * @implNote POST
     * @implNote just substract the mone
     * @param checkoutSum the checkout sum
     * @return the new balance
     */
    double checkout(double checkoutSum);
}
