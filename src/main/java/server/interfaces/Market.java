package server.interfaces;


import java.util.UUID;

/**
 * The interface Market.
 */
public interface Market {
    // product management-------------------------------------------------------------

    /**
     * Get all products
     *
     * @implNote GET
     * @return the object[][]={ {productID, productName, price, availableUnits}, ... }
     */
    Object[][] getAllProducts();

    /**
     * Add product uuid. Creates a new product entry if no product with the same name exists, returns the productID in both cases
     *
     * @implNote POST
     * @param productName the product name
     * @return the productID
     */
    UUID addProduct(String productName);

    /**
     * deletes the given product as identified per its productID
     *
     * @implNote DELETE
     * @param productID the productID of the product that will be deleted
     */
    void deleteProductEntry(UUID productID);

    // trading -----------------------------------------------------------------------

    /**
     * Sell.
     *
     * @implNote POST
     * @throws java.util.NoSuchElementException if user has no such product
     * @param userID  the userID
     * @param productID the productID
     */
    void sell(UUID userID, UUID productID);

    /**
     * Buy.
     *
     * @implNote POST
     * @throws java.util.MissingResourceException if user
     * @param userID   the userID
     * @param productID the productID
     * @return the price
     */
    double buy(UUID userID, UUID productID);


}
