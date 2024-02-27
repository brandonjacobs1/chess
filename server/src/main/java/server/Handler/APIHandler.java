package server.Handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import server.BadRequestException;
import server.NotAuthenticatedException;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class APIHandler {
    public Gson serializer;
    public UserService userService;
    public GameService gameService;
    public UserHandler userHandler;
    public ClearHandler clearHandler;
    public GameHandler gameHandler;
    public APIHandler () {
        serializer = new Gson();

        // Define services
        userService = new UserService();
        gameService = new GameService();
    }

    public void validateBody(Record data, List<String> dataKeys) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, BadRequestException {
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
}
