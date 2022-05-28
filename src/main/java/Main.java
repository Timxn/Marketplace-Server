import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import server.implementation.Users;

import javax.management.InstanceAlreadyExistsException;
import java.util.NoSuchElementException;
import java.util.UUID;

import static spark.Spark.*;

public class Main {
    private static Users users = new Users();
    // http://localhost:4567/hello
    public static void main(String[] args) {
        before(((request, response) -> {
            JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
            if (!(request.pathInfo().equals("/user/register")) && !(request.pathInfo().equals("/user/login"))) {
                try {
                    users.checkToken(UUID.fromString(requestJSON.get("token").toString().replace("\"","")));
                } catch (NoSuchElementException e) {
                    halt(401, "Unauthorized");
                }
            }
        }));
        path("/user", () -> {
            post("/register", (request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
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
            post("/deposit", ((request, response) -> {
                JsonObject requestJSON = null;
                try {
                    requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                } catch (JsonParseException e) {
                    JsonObject exampleJson = new JsonObject();
                    exampleJson.addProperty("token", "TOKEN");
                    exampleJson.addProperty("value", 42);
                    return exampleJson.toString();
                }
                UUID token = UUID.fromString((requestJSON.get("token").getAsString()));
                double value = requestJSON.get("value").getAsDouble();
                double newBalance = users.deposit(value, token);
                JsonObject returnJSON= new JsonObject();
                returnJSON.addProperty("balance", newBalance);
                return returnJSON.toString();
            }));
            post("/withdraw", ((request, response) -> {
                JsonObject requestJSON = null;
                try {
                    requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                } catch (JsonParseException e) {
                    return exampleJsonWithdraw();
                }
                UUID token = UUID.fromString((requestJSON.get("token").getAsString()));
                double value = 0;
                try {
                    value = requestJSON.get("value").getAsDouble();
                } catch (NumberFormatException e) {
                    return exampleJsonWithdraw();
                }
                double newBalance = users.withdraw(value, token);
                JsonObject returnJSON= new JsonObject();
                returnJSON.addProperty("userID", users.checkToken(token).toString());
                returnJSON.addProperty("balance", newBalance);
                return returnJSON.toString();
            }));
        });
    }

    private static String exampleJsonWithdraw() {
        JsonObject exampleJson = new JsonObject();
        exampleJson.addProperty("token", "TOKEN");
        exampleJson.addProperty("value", 42);
        return exampleJson.toString();
    }
}