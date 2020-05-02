package server.controllers;

import io.javalin.http.Handler;

public class UserController {

    public Handler getUser = ctx -> {
        // Hent brugerdata fra database og returner
    };

    public Handler newUser = ctx -> {
        // Opret ny bruger i databasen
    };

    public Handler updateUser = ctx -> {
        // Opdater bruger. Relevant ift. skift af kodeord og indtastning af API-nøgler
    };
    public Handler addApiKey = ctx -> {

    };
}
