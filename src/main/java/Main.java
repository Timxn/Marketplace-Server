import static spark.Spark.*;

public class Main {
    // http://localhost:4567/hello
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
        System.out.println("DEBUG");
    }
}