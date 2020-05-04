package server.controllers;

import DALleValle.DatabaseController;
import DALleValle.DatabaseException;
import DALleValle.UserDTO;
import io.javalin.http.Handler;
import io.javalin.http.InternalServerErrorResponse;

public class UserController {

    public Handler getUser = ctx -> {
        UserDTO user;

        try {
            String username = ctx.pathParam("username");
            user = DatabaseController.getInstance().getUser(username);
        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }

        ctx.status(200);
        ctx.header("Content-Type", "application/json");
        // TODO: skal password sendes med her?
        ctx.json(user);
    };

    public Handler newUser = ctx -> {
        try {

        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }
    };

    public Handler updateUser = ctx -> {
        try {

        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }
    };
    public Handler addApiKey = ctx -> {
        try {

        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }
    };
}
