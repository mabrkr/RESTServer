package server.controllers;

import DAO.DatabaseConnection;
import io.javalin.http.Handler;

public class UserController {

    private DatabaseConnection database;

    public Handler getUser = ctx -> {

    };

    public Handler newUser = ctx -> {

    };

    public Handler updateUser = ctx -> {

    };

    public UserController(DatabaseConnection database) {
        this.database = database;
    }
}
