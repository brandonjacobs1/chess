package server;

import com.google.gson.Gson;
import spark.*;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    public ArrayList<String> names = new ArrayList<>();

    private Object listNames(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson(Map.of("name", names));
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/name/:name", new Route() {
            public Object handle(Request req, Response res) {
                names.add(req.params(":name"));
                return listNames(req, res);
            }
        });

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
