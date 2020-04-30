package server.controllers;

import DAO.DatabaseConnection;

public class UserController {

    private DatabaseConnection database;

    public UserController(DatabaseConnection database) {
        this.database = database;
    }
}
