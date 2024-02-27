package server.Handler;

import com.google.gson.Gson;
import server.BadRequestException;
import server.NotAuthenticatedException;
import service.DuplicateEntryException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class ErrorHandler {


    public Object errorHandler(Exception e, Request req, Response res) {
        String message;
        int statusCode;
        switch (e) {
            case BadRequestException badRequestException -> {
                message = String.format("Error: %s", "bad request");
                statusCode = 400;
            }
            case NotAuthenticatedException notAuthenticatedException -> {
                message = String.format("Error: %s", "unauthorized");
                statusCode = 401;
            }
            case DuplicateEntryException duplicateEntryException -> {
                message = String.format("Error: %s", "already taken");
                statusCode = 403;
            }
            case null, default -> {
                assert e != null;
                message = String.format("Error: %s", e.getMessage());
                statusCode = 500;
            }
        }

        var body = new Gson().toJson(Map.of("message", message, "success", false));
        res.type("application/json");
        res.status(statusCode);
        res.body(body);
        return body;
    }
}
