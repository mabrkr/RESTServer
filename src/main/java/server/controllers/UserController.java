package server.controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import dal.ApiKeyDTO;
import dal.DatabaseController;
import dal.DatabaseException;
import dal.UserDTO;
import io.javalin.http.Handler;
import io.javalin.http.InternalServerErrorResponse;

public class UserController {

    public Handler authenticateUser = ctx -> {
        AuthorizationController.authorize(ctx);
        UserDTO user;
        try {
            String username = ctx.basicAuthCredentials().getUsername();
            user = DatabaseController.getInstance().getUser(username);
        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }

        ctx.status(200);
        ctx.header("Content-Type", "application/json");
        ctx.json(user);
    };

    public Handler getUser = ctx -> {
        AuthorizationController.authorize(ctx);

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

    // No authorization.
    public Handler newUser = ctx -> {
        UserDTO user;

        try {
            user = new Gson().fromJson(ctx.body(), UserDTO.class);
            DatabaseController.getInstance().createNewUser(user);
        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }

        ctx.status(201);
        ctx.header("Content-Type", "application/json");
        ctx.json(user);
    };

    public Handler updateUser = ctx -> {
        AuthorizationController.authorize(ctx);

        UserDTO user;

        try {

            user = new Gson().fromJson(ctx.body(), UserDTO.class);
            DatabaseController.getInstance().updateUser(user);
        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }

        ctx.status(200);
        ctx.header("Content-Type", "application/json");
        ctx.json(user);
    };

    public Handler addApiKey = ctx -> {
        AuthorizationController.authorize(ctx);

        ApiKeyDTO key;

        try {
            String username = ctx.pathParam("username");
            key = new Gson().fromJson(ctx.body(), ApiKeyDTO.class);
            DatabaseController.getInstance().createOrUpdateApiKey(username, key);

        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }

        ctx.status(201);
        ctx.header("Content-Type", "application/json");
        ctx.json(key);
    };
}
