package de.oose.webservice.server;

import com.google.gson.*;
import de.oose.webservice.server.implementation.ShopManager;

import javax.management.InstanceAlreadyExistsException;
import java.util.*;

import static spark.Spark.*;

public class RestAPI {
    private static ShopManager shopManager = new ShopManager();
    private static final String URL = "http://localhost:4567";

    public static void main(String[] args) {
        before(((request, response) -> {
            JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
            if (!(request.pathInfo().equals("/user/register")) && !(request.pathInfo().equals("/user/login"))) {
                try {
                    shopManager.getUserIDByToken(UUID.fromString(requestJSON.get("token").getAsString()));
                } catch (NoSuchElementException e) {
                    halt(401, "Unauthorized");
                }
            }
        }));
        path("/user", () -> {
            get("", ((request, response) -> {
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("post", URL + "/user/register");
                responseJson.addProperty("post", URL + "/user/login");
                responseJson.addProperty("delete", URL + "/user/logout");
                responseJson.addProperty("delete", URL + "/user/removeUser");
                responseJson.addProperty("post", URL + "/user/addMoney");
                responseJson.addProperty("post", URL + "/user/balance");
                responseJson.addProperty("post", URL + "/user/depot");
                responseJson.addProperty("post", URL + "/market/products");
                responseJson.addProperty("post", URL + "/market/addProduct");
                responseJson.addProperty("delete", URL + "/market/deleteProduct");
                responseJson.addProperty("post", URL + "/market/addProductToMarket");
                responseJson.addProperty("post", URL + "/market/sell");
                responseJson.addProperty("post", URL + "/market/buy");
                response.status(200);
                return responseJson.toString();
            }));
            post("/register", (request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                if (requestJSON.get("mail") == null || requestJSON.get("password") == null) {
                    response.status(400);
                    return exampleJSONWithUsernameAndPassword();
                }
                try {
                    shopManager.register(requestJSON.get("mail").getAsString(), requestJSON.get("password").getAsString());
                    System.out.println("registered");
                    response.status(201);
                } catch (InstanceAlreadyExistsException e) {
                    response.status(405);
                }
                return response.status();
            });
            post("/login", (request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                UUID token;
                try {
                    token = shopManager.login(requestJSON.get("mail").getAsString(), requestJSON.get("password").getAsString());
                    response.status(201);
                    response.type("application/json; charset=utf-8");
                    JsonObject returnElement = new JsonObject();
                    returnElement.addProperty("token", token.toString());
                    System.out.println("login");
                    return returnElement.toString();
                } catch (NoSuchElementException e) {
                    response.status(405);
                    return response.status();
                }
            });
            delete("/logout", (request, response) -> {
                JsonObject requestJSON = null;
                try {
                    requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                } catch (JsonParseException e) {
                    JsonObject exampleJson = new JsonObject();
                    exampleJson.addProperty("token", "TOKEN");
                    return exampleJson.toString();
                }
                shopManager.logout(UUID.fromString((requestJSON.get("token").getAsString())));
                response.status(200);
                System.out.println("logout");
                return response.status();
            });
            delete("/removeUser", ((request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                shopManager.deleteAccount(UUID.fromString(requestJSON.get("token").getAsString()));
                response.status(200);
                return response.status();
            }));
            post("/addMoney", (request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                shopManager.setBalance(UUID.fromString(requestJSON.get("token").getAsString()), Double.valueOf(requestJSON.get("value").getAsString()));
                response.status(200);
                return response.status();
            });
            post("/balance", (request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                double tmp = shopManager.getBalance(UUID.fromString(requestJSON.get("token").getAsString()));
                response.status(200);
                JsonObject exampleJson = new JsonObject();
                exampleJson.addProperty("balance", tmp);
                return exampleJson.toString();
            });
            post("/depot", (request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                UUID tok = UUID.fromString(requestJSON.get("token").getAsString());
                HashMap<String, Integer> depot = shopManager.getDepot(tok);
                JsonObject json = new JsonObject();
                for (Map.Entry<String, Integer> entry: depot.entrySet()) {
                    JsonObject tmp = new JsonObject();
                    tmp.addProperty("count", depot.get(entry.getKey()));
                    json.add(entry.getKey(), tmp);
                }
                response.status(200);
                return json.toString();
            });
        });
        path("/market", () -> {
            post("/products", (request, response) -> {
                HashMap<String, Integer> offers = shopManager.getAllOffers();
                HashMap<String, Double> prices = shopManager.getAllPrices();
                JsonObject json = new JsonObject();
                for (Map.Entry<String, Integer> entry: offers.entrySet()) {
                    JsonObject tmp = new JsonObject();
                    tmp.addProperty("count", offers.get(entry.getKey()));
                    tmp.addProperty("price", prices.get(entry.getKey()));
                    json.add(entry.getKey(), tmp);
                }
                response.status(200);
                return json.toString();
            });
            post("/addProduct", ((request, response) -> {
                JsonObject requestJSON = null;
                try {
                    requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                } catch (JsonParseException e) {
                    return exampleJsonWithTokenAndValue();
                }
                String name = null;
                try {
                    name = requestJSON.get("value").getAsString();
                } catch (NumberFormatException e) {
                    return exampleJsonWithTokenAndValue();
                }
//                markt.addProduct(name);
                response.status(401);
                return "Done!";
            }));
            post("/buy", ((request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                shopManager.buyProduct(requestJSON.get("productname").getAsString(), Integer.valueOf(requestJSON.get("count").toString()), UUID.fromString(requestJSON.get("token").getAsString()));
                response.status(200);
                return "";
            }));
            post("/addProductToMarket", ((request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                shopManager.addProductToOffers(requestJSON.get("productname").getAsString(), Integer.valueOf(requestJSON.get("count").toString()));
                response.status(200);
                return "";
            }));
            post("/sell", ((request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                shopManager.sellProduct(requestJSON.get("productname").getAsString(), Integer.valueOf(requestJSON.get("count").toString()), UUID.fromString(requestJSON.get("token").getAsString()));
                response.status(200);
                return "";
            }));
        });
    }

    private static String exampleJSONWithUsernameAndPassword() {
        JsonObject exampleJson = new JsonObject();
        exampleJson.addProperty("mail", "MAIL");
        exampleJson.addProperty("password", "PASSWORD");
        return exampleJson.toString();
    }
    private static String exampleJsonWithTokenAndValue() {
        JsonObject exampleJson = new JsonObject();
        exampleJson.addProperty("token", "TOKEN");
        exampleJson.addProperty("value", 42);
        return exampleJson.toString();
    }
    private static String returnJSONWithUserIDAndBalance(double value) {
        JsonObject returnJSON= new JsonObject();
        returnJSON.addProperty("balance", value);
        return returnJSON.toString();
    }
}
