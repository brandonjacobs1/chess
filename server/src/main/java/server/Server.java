package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.*;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.get("/error", this::throwError);
        Spark.get("/data", this::data);
        Spark.get("/badReq", this::badReq);
        Spark.get("/noauth", this::noauth);

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
            statusCode = 401;
        } else {
            statusCode = 500;
        }

        var body = new Gson().toJson(Map.of("message", message, "success", false));
        res.type("application/json");
        res.status(statusCode);
        res.body(body);
        return body;
    }
    private Object throwError(Request req, Response res) {
        throw new RuntimeException("Internal server error");
    }
    private Object data(Request req, Response res) throws DataAccessException {
        throw new DataAccessException("Data not found");
    }
    private Object badReq(Request req, Response res) throws BadRequestException {
        throw new BadRequestException("Bad request");
    }
    private Object noauth(Request req, Response res) throws NotAuthenticatedException {
        throw new NotAuthenticatedException("Not authenticated");
    }
}
