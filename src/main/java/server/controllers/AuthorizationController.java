package server.controllers;

import dal.DatabaseController;
import dal.DatabaseException;
import io.javalin.http.Handler;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.UnauthorizedResponse;

public class AuthorizationController {

    public Handler authorize = ctx -> {
        try {
            if (!ctx.basicAuthCredentialsExist()) {
                throw new UnauthorizedResponse("No authorization credentials found.");
            }

            // Get username and password from the basic auth header
            String username = ctx.basicAuthCredentials().getUsername();
            String password = ctx.basicAuthCredentials().getPassword();

            System.out.println(username + " : " + password);
            if (!DatabaseController.getInstance().authenticateUser(username, password)) {
                throw new UnauthorizedResponse("Wrong username and/or password");
            }
        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error: " + e.getMessage());
        }
    };

    // To be used if a change to token-based authentication is made.
    public Handler login = ctx -> {
    };

    // To be used if a change to token-based authentication is made.
    public Handler logout = ctx -> {
    };
}
