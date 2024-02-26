package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.*;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static spark.Spark.halt;

public class Server {
    UserService userService = new UserService();
    Gson serializer = new Gson();
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

    private void validateBody(Record data, List<String> dataKeys) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, BadRequestException {
        ArrayList<String> emptyItems = new ArrayList<>();
        for(String key : dataKeys) {
            Method getter = data.getClass().getMethod(key);
            Object value = getter.invoke(data);
            if (value == null) {
                emptyItems.add(key);
            }
        }
        if (!emptyItems.isEmpty()) {
            throw new BadRequestException(generateInvalidBodyErrorMessage(emptyItems));
        }
    }

    private String generateInvalidBodyErrorMessage(ArrayList<String> missingKeys) {
        StringBuilder message = new StringBuilder("Missing ");
        if (missingKeys.size() == 1) {
            message.append(missingKeys.getFirst());
        } else if (missingKeys.size() == 2) {
            message.append(missingKeys.get(0)).append(" and ").append(missingKeys.get(1));
        } else {
            for (int i = 0; i < missingKeys.size(); i++) {
                message.append(missingKeys.get(i));
                if (i < missingKeys.size() - 2) {
                    message.append(", ");
                } else if (i == missingKeys.size() - 2) {
                    message.append(", and ");
                }
            }
        }
        message.append(" in request body.");
        return message.toString();
    }

    private Object registerHandler(Request req, Response res) throws DataAccessException, BadRequestException {
        try {
            // validate body
            UserData user;
            try {
                user = serializer.fromJson(req.body(), UserData.class);
            } catch (Exception e) {
                throw new BadRequestException("There was an error in your JSON body");
            }
            List<String> keysToValidate = Arrays.asList("username", "password", "email");
            validateBody(user, keysToValidate);
            // send to service
            AuthData authData = userService.register(user);
            // return a response
            return serializer.toJson(authData);
        } catch (DataAccessException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }
    private Object loginHandler(Request req, Response res) throws BadRequestException, DataAccessException {
        try {
            // validate body
            UserData user;
            try {
                user = serializer.fromJson(req.body(), UserData.class);
            } catch (Exception e) {
                throw new BadRequestException("There was an error in your JSON body");
            }
            List<String> keysToValidate = Arrays.asList("username", "password");
            validateBody(user, keysToValidate);
            // send to service
            AuthData authData = userService.login(user);
            // return a response
            return serializer.toJson(authData);
        } catch (DataAccessException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }    }
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
