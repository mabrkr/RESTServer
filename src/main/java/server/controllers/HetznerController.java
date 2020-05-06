package server.controllers;

import io.javalin.http.Handler;
import kong.unirest.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controller for handling the Digital Ocean RESTful API.
 * The general structure of the different endpoint handlers is an HTTP-request using UniRest
 * followed by a response via the Javalin server.
 */
public class HetznerController {

    // See constructor below for configuration
    private UnirestInstance unirest;

    public Handler getServers = ctx -> {
        String key = ctx.header("API-key");

        HttpResponse<JsonNode> response = unirest.get("/servers")
                .header("Authorization", "Bearer " + key)
                .asJson();

        ctx.status(response.getStatus());
        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler getServer = ctx -> {
        String key = ctx.header("API-key");

        HttpResponse<JsonNode> response = unirest.get("/servers/{id}")
                .routeParam("id", ctx.pathParam("id"))
                .header("Authorization", "Bearer " + key)
                .asJson();

        ctx.status(response.getStatus());
        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler createServer = ctx -> {
        String key = ctx.header("API-key");

        HttpResponse<JsonNode> response = unirest.post("/servers")
                .header("Authorization", "Bearer " + key)
                .body(ctx.body())
                .asJson();

        ctx.status(response.getStatus());
        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler deleteServer = ctx -> {
        String key = ctx.header("API-key");

        HttpResponse response = unirest.delete("/servers/{id}")
                .routeParam("id", ctx.pathParam("id"))
                .header("Authorization", "Bearer " + key)
                .asEmpty();

        ctx.status(response.getStatus());
    };

    public HetznerController() {
        unirest = Unirest.spawnInstance();
        unirest.config()
                .addShutdownHook(true)
                .setDefaultHeader("Accept", "application/json")
                .setDefaultHeader("Content-Type", "application/json")
                .defaultBaseUrl("https://api.hetzner.cloud/v1");
    }
}
