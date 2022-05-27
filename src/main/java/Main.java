import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.implementation.Users;

import javax.management.InstanceAlreadyExistsException;
import java.util.UUID;

import static spark.Spark.*;

public class Main {
    private static Users users = new Users();
    // http://localhost:4567/hello
    public static void main(String[] args) {
        post("/register", (request, response) -> {
            System.out.println("DEBUG");
            JsonObject requestJSON = new JsonParser().parse(request.body()).getAsJsonObject();
            System.out.println(requestJSON.get("mail"));
            String username = requestJSON.get("mail").getAsString();
            response.type("text/json");
            try {
                users.register(requestJSON.get("mail").getAsString(), requestJSON.get("password").getAsString());
            } catch (InstanceAlreadyExistsException e) {
                return "ERROR";
            }
                /*
                curl -X POST http://localhost:4567/register -H 'Cache-Control: no-cache' -H 'Content-Type: application/json' -d '{"username" : "dieter"}'
                 */
            return "TEST";
        });
        path("/user", () -> {
            post("/login", ((request, response) -> {
                response.type("text/json");
                JsonObject temp = new JsonObject();
                temp.addProperty("alter", 42);
                temp.addProperty("name", "dieter");
                return temp;
            }));
        });

    }
}