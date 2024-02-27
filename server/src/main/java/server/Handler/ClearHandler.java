package server.Handler;

import spark.Request;
import spark.Response;

public class ClearHandler extends APIHandler{

    public Object clearHandler(Request req, Response res) {
        userService.clear();
        gameService.clear();
        return "{}";
    }
}
