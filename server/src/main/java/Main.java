import chess.*;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import server.Server;
public class Main {
    public static void main(String[] args) throws DataAccessException {
        Server server = new Server();
        int port = server.run(8080);
        System.out.println("Server running on port " + port);
        DatabaseManager.setup();
    }
}