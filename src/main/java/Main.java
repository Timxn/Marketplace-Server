import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
                if ((users.checkToken(UUID.fromString(requestJSON.get("token").toString().replace("\"","")))).equals(null)) halt(401, "Unauthorized");
            }
        }));
        path("/user", () -> {
            post("/register", (request, response) -> {
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                String username = requestJSON.get("mail").getAsString();
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
                JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
                users.logout(UUID.fromString((requestJSON.get("token").toString()).replace("\"","")));
                response.status(200);
                return response.status();
            });
        });
//        path("/user", () -> {
//            post("/login", ((request, response) -> {
//                response.type("text/json");
//                JsonObject temp = new JsonObject();
//                temp.addProperty("alter", 42);
//                temp.addProperty("name", "dieter");
//                return temp;
//            }));
//        });

    }
}