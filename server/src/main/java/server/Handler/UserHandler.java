package server.Handler;

import model.AuthData;
import model.UserData;
import server.BadRequestException;
import server.NotAuthenticatedException;
import service.DuplicateEntryException;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.List;

public class UserHandler extends APIHandler{

    public boolean authenticate(String token) throws NotAuthenticatedException {
        return userService.authenticate(token);
    }

    public Object registerHandler(Request req, Response res) throws BadRequestException, DuplicateEntryException {
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
        } catch (BadRequestException | DuplicateEntryException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }
    public Object loginHandler(Request req, Response res) throws BadRequestException, NotAuthenticatedException {
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
        } catch (BadRequestException | NotAuthenticatedException e ) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }
    public Object logoutHandler(Request req, Response res) {
        try {
            String token = req.headers("authorization");
            // send to service
            userService.logout(token);
            // return a response
            return "{}";
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }
}
