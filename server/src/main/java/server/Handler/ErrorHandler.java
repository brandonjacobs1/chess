package server.Handler;

import com.google.gson.Gson;
import server.BadRequestException;
import server.NotAuthenticatedException;
import server.DuplicateEntryException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class ErrorHandler {


    public Object errorHandler(Exception e, Request req, Response res) {
        String message;
        int statusCode;
        if (e instanceof BadRequestException) {
            message = String.format("Error: %s", "bad request");
            statusCode = 400;
        } else if (e instanceof NotAuthenticatedException) {
            message = String.format("Error: %s", "unauthorized");
            statusCode = 401;
        } else if (e instanceof DuplicateEntryException){
            message = String.format("Error: %s", "already taken");
            statusCode = 403;
        } else {
            message = String.format("Error: %s", e.getMessage());
            statusCode = 500;
        }

        var body = new Gson().toJson(Map.of("message", message, "success", false));
        res.type("application/json");
        res.status(statusCode);
        res.body(body);
        return body;
    }
}
