package interfaces;

import java.util.UUID;

public interface Users {
    // create new Account
    void register(String mail, String password);

    // login -> returns token generated by UUID
    UUID login(String mail, String password);

    // Log out by deleting the specified Token
    void logout(UUID token);
}
