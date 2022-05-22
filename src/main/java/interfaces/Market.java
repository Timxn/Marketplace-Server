package interfaces;

import java.util.UUID;

public interface Market {
    // sell something
    void sell(UUID sellerID, UUID productID);

    // buy something from whoever has it, then return the price for what it was bought
    double buy(UUID buyerID, UUID productID);

    // get all offered products (if implemented as client <-> client)
    // return { {productID, productName, price}, ... }
    Object[][] getAllOffers();

    Object[][] gettAllOffersPerUser(UUID userID);
}
