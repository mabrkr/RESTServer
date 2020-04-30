package server.controllers;

import DAO.DatabaseConnection;
import io.javalin.http.Handler;

public class SessionController {

    private DatabaseConnection database;

    public Handler login = ctx -> {

    };

    public Handler logout = ctx -> {

    };

    public SessionController(DatabaseConnection database) {
        this.database = database;
    }
}
