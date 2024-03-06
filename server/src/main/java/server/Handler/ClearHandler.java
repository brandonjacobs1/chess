package server.Handler;

import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public class ClearHandler extends APIHandler{

    public Object clearHandler(Request req, Response res) {
        try {
            userService.clear();
            gameService.clear();
            return "{}";
        } catch (DataAccessException e) {
            throw new RuntimeException("Internal server error");
        }
    }
}
