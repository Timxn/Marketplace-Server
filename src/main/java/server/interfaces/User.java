package server.interfaces;

import java.util.UUID;

public interface User {
    double getBalance();

    void setBalance(double balance);

    UUID getUserID();

    String getMail();

    String getPassword();
}
