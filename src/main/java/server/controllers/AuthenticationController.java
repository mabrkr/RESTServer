package server.controllers;

import dal.DatabaseController;
import dal.DatabaseException;
import io.javalin.http.Handler;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.UnauthorizedResponse;

public class AuthenticationController {

    public Handler authenticate = ctx -> {
        try {
            if (!ctx.basicAuthCredentialsExist()) {
                throw new UnauthorizedResponse("No basic authorization credentials found.");
            }

            // Get username and password from the basic auth header
            String username = ctx.basicAuthCredentials().getUsername();
            String password = ctx.basicAuthCredentials().getPassword();
            if (!DatabaseController.getInstance().authenticateUser(username, password)) {
                throw new UnauthorizedResponse("Wrong username and/or password");
            }
        } catch (DatabaseException e) {
            throw new InternalServerErrorResponse("Server database error.");
        }
    };

    /*
     * To Be used if a change to token-based authentication is made.
     */
    public Handler login = ctx -> {
        /*
         * Tjek brugernavn + kodeord i database. Hvis ja: generer token og put den i database og returner den.
         * Hvis nej: returner passende fejlkode.
         */
    };

    /*
     * To Be used if a change to token-based authentication is made.
     */
    public Handler logout = ctx -> {
        /*
         * Slet token i databasen og returner 204. Tokens skal udløbe automatisk efter et stykke tid også.
         */
    };
}
