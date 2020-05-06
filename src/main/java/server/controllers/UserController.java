package server.controllers;

import dal.ApiKeyDTO;
import dal.DatabaseController;
import dal.DatabaseException;
import dal.UserDTO;
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
        ctx.json(user);
    };

    public Handler newUser = ctx -> {
        UserDTO user;

        try {
             user = ctx.bodyAsClass(UserDTO.class);
             DatabaseController.getInstance().createNewUser(user);
        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }

        ctx.status(201);
        ctx.header("Content-Type", "application/json");
        ctx.json(user);
    };

    public Handler updateUser = ctx -> {
        UserDTO user;

        try {
            user = ctx.bodyAsClass(UserDTO.class);
            DatabaseController.getInstance().updateUser(user);
        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }

        ctx.status(200);
        ctx.header("Content-Type", "application/json");
        ctx.json(user);
    };

    public Handler addApiKey = ctx -> {
        ApiKeyDTO key;

        try {
            String username = ctx.pathParam("username");
            key = ctx.bodyAsClass(ApiKeyDTO.class);
            DatabaseController.getInstance().createOrUpdateApiKey(username, key);

        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }

        ctx.status(201);
        ctx.header("Content-Type", "application/json");
        ctx.json(key);
    };
}
