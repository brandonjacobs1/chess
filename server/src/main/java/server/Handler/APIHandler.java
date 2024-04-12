package server.Handler;

import com.google.gson.Gson;
import server.BadRequestException;
import service.GameService;
import service.UserService;
import webSocket.WebSocketHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class APIHandler {
    public Gson serializer;
    public UserService userService;
    public GameService gameService;
    public WebSocketHandler webSocketHandler;
    public APIHandler (){
        serializer = new Gson();

        // Define services
        userService = new UserService();
        gameService = new GameService();
    }

    public void setWebSocketHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
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
