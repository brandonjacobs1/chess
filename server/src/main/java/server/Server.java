package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.*;

import java.util.ArrayList;
import java.util.Map;

import static spark.Spark.halt;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.before((req, res) -> {
            if (!req.pathInfo().equals("/user") && !req.pathInfo().equals("/session")) {
                boolean isAuthenticated = false;
                if(!isAuthenticated) {
                    halt(401, "Not authenticated. Please log in.");
                }
            }
        });

        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.delete("/db", this::clearHandler);
        Spark.get("/game", this::listGameHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.post("/game", this::createGameHandler);


        Spark.exception(Exception.class, this::errorHandler);
        Spark.notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler(new Exception(msg), req, res);
        });

        Spark.awaitInitialization();
        return Spark.port();
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Object errorHandler(Exception e, Request req, Response res) {
        String message = String.format("Error: %s", e.getMessage());
        int statusCode;
        if (e instanceof BadRequestException) {
            statusCode = 400;
        } else if (e instanceof NotAuthenticatedException) {
            statusCode = 401;
        } else if (e instanceof DataAccessException){
            statusCode = 404;
        } else {
            statusCode = 500;
        }

        var body = new Gson().toJson(Map.of("message", message, "success", false));
        res.type("application/json");
        res.status(statusCode);
        res.body(body);
        return body;
    }
    private Object registerHandler(Request req, Response res) {
        // validate body

        // send to service

        // return a response
        throw new RuntimeException("Internal server error");
    }
    private Object loginHandler(Request req, Response res) {
        throw new RuntimeException("Internal server error");
    }
    private Object logoutHandler(Request req, Response res) {
        throw new RuntimeException("Internal server error");
    }
    private Object clearHandler(Request req, Response res) {
        throw new RuntimeException("Internal server error");
    }
    private Object createGameHandler(Request req, Response res) {
        throw new RuntimeException("Internal server error");
    }
    private Object listGameHandler(Request req, Response res) {
        throw new RuntimeException("Internal server error");
    }
    private Object joinGameHandler(Request req, Response res) {
        throw new RuntimeException("Internal server error");
    }

}
