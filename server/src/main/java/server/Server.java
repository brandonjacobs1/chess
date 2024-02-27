package server;

import server.Handler.*;
import spark.*;
import java.util.*;

public class Server {
    UserHandler userHandler;
    ClearHandler clearHandler;
    GameHandler gameHandler;
    ErrorHandler errorHandler;
    public Server() {
        userHandler = new UserHandler();
        clearHandler = new ClearHandler();
        gameHandler = new GameHandler();
        errorHandler = new ErrorHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Authenticate all routes that require authentication
        Spark.before((req, res) -> {
            if (!req.pathInfo().equals("/user") && !req.pathInfo().equals("/db") && !(req.pathInfo().equals("/session") && Objects.equals(req.requestMethod(), "POST"))) {
                boolean isAuthenticated = userHandler.authenticate(req.headers("authorization"));
                if(!isAuthenticated) {
                    throw new NotAuthenticatedException("unauthorized");
                }
            }
        });

        // Define routes
        Spark.post("/user", (req, res) -> userHandler.registerHandler(req, res));
        Spark.post("/session", (req, res) -> userHandler.loginHandler(req, res));
        Spark.delete("/session", (req, res) -> userHandler.logoutHandler(req, res));
        Spark.delete("/db", (req, res) -> clearHandler.clearHandler(req, res));
        Spark.get("/game", (req, res) -> gameHandler.listGameHandler(req, res));
        Spark.put("/game", (req, res) -> gameHandler.joinGameHandler(req, res));
        Spark.post("/game", (req, res) -> gameHandler.createGameHandler(req, res));

        // Handle exceptions
        Spark.exception(Exception.class, (exception, request, response) -> errorHandler.errorHandler(exception, request, response));

        // Handle routes not found
        Spark.notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler.errorHandler(new Exception(msg), req, res);
        });

        Spark.awaitInitialization();
        return Spark.port();
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
