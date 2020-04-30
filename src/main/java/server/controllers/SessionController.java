package server.controllers;

import DAO.DatabaseConnection;

public class SessionController {

    private DatabaseConnection database;

    public SessionController(DatabaseConnection database) {
        this.database = database;
    }
}
