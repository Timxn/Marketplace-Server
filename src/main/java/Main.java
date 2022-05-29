import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import server.implementation.Users;
//import server.implementation.Market;

import javax.management.InstanceAlreadyExistsException;
import java.util.NoSuchElementException;
import java.util.UUID;

import static spark.Spark.*;

public class Main {
    private static Users users = new Users();
//    private static Market markt = new Market();
    // http://localhost:4567/hello
    public static void main(String[] args) {
        before(((request, response) -> {
            JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
            if (!(request.pathInfo().equals("/user/register")) && !(request.pathInfo().equals("/user/login"))) {
                try {
                    users.checkToken(UUID.fromString(requestJSON.get("token").getAsString()));
                } catch (NoSuchElementException e) {
                    halt(401, "Unauthorized");
                }
            }
        }));
        path("/user", () -> {
            post("/register", (request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                if (requestJSON.get("mail") == null || requestJSON.get("password") == null) {
                    response.status(400);
                    return exampleJSONWithUsernameAndPassword();
                }
                try {
                    users.register(requestJSON.get("mail").getAsString(), requestJSON.get("password").getAsString());
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
                    token = users.login(requestJSON.get("mail").getAsString(), requestJSON.get("password").getAsString());
                    response.status(201);
                    JsonObject returnElement = new JsonObject();
                    returnElement.addProperty("token", token.toString());
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
                users.logout(UUID.fromString((requestJSON.get("token").getAsString())));
                response.status(200);
                return response.status();
            });
            put("/deposit", ((request, response) -> {
                JsonObject requestJSON = null;
                try {
                    requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                } catch (JsonParseException e) {
                    response.status(400);
                    return exampleJsonWithTokenAndValue();
                }
                double value = 0;
                try {
                    value = requestJSON.get("value").getAsDouble();
                } catch (NumberFormatException e) {
                    response.status(400);
                    return exampleJsonWithTokenAndValue();
                }
                UUID token = UUID.fromString((requestJSON.get("token").getAsString()));
                double newBalance = users.deposit(value, token);
                response.status(200);
                return returnJSONWithUserIDAndBalance(newBalance);
            }));
            put("/withdraw", ((request, response) -> {
                JsonObject requestJSON = null;
                try {
                    requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                } catch (JsonParseException e) {
                    return exampleJsonWithTokenAndValue();
                }
                double value = 0;
                try {
                    value = requestJSON.get("value").getAsDouble();
                } catch (NumberFormatException e) {
                    return exampleJsonWithTokenAndValue();
                }
                UUID token = UUID.fromString((requestJSON.get("token").getAsString()));
                double newBalance = users.withdraw(value, token);
                response.status(200);
                return returnJSONWithUserIDAndBalance(newBalance);
            }));
        });
        path("/market", () -> {
            get("/products", (request, response) -> {
//                markt.getAllProducts();
                response.status(501);
                return "WIP";
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
            delete("/deleteProduct", ((request, response) -> {
                response.status(501);
                return "WIP";
            }));
            post("/sell", ((request, response) -> {
                response.status(501);
                return "WIP";
            }));
            post("/buy", ((request, response) -> {
                response.status(501);
                return "WIP";
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