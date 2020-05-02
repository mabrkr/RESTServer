package server.controllers;

import io.javalin.http.Handler;

public class UserController {

    public Handler getUser = ctx -> {
        // Hent brugerdata fra database og returner.
    };

    public Handler newUser = ctx -> {
        // Opret ny bruger i databasen.
    };

    public Handler updateUser = ctx -> {
        // Opdater bruger. Relevant ift. skift af kodeord.
    };
    public Handler addApiKey = ctx -> {
        // Tilføj api nøgle til bruger. Request body skal indeholde nødvendig info (host/registrar og nøgle).
    };
}
