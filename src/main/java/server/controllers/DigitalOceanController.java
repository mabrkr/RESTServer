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
public class DigitalOceanController {

    // See constructor below for configuration
    private UnirestInstance unirest;

    public Handler getDroplets = ctx -> {
        String key = ctx.header("API-Key");

        HttpResponse<JsonNode> response = unirest.get("/droplets")
                .header("Authorization", "Bearer " + key)
                .asJson();

        ctx.status(response.getStatus());
        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler getAccountInfo = ctx -> {
        String key = ctx.header("API-Key");

        HttpResponse<JsonNode> response = unirest.get("/account")
                .header("Authorization", "Bearer "+ key)
                .asJson();

        ctx.status(response.getStatus());
        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler getDroplet = ctx -> {
        String key = ctx.header("API-Key");

        HttpResponse<JsonNode> response = unirest.get("/droplets/{id}")
                .routeParam("id", ctx.pathParam("id"))
                .header("Authorization", "Bearer "+ key)
                .asJson();

        ctx.status(response.getStatus());
        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler createDroplet = ctx -> {
        String key = ctx.header("API-Key");

        HttpResponse<JsonNode> response = unirest.post("/droplets")
                .header("Authorization", "Bearer "+ key)
                .body(ctx.body())
                .asJson();

        ctx.status(response.getStatus());
        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler deleteDroplet = ctx -> {
        String key = ctx.header("API-Key");

        HttpResponse response = unirest.delete("/droplets/{id}")
                .routeParam("id", ctx.pathParam("id"))
                .header("Authorization", "Bearer "+ key)
                .asEmpty();

        ctx.status(response.getStatus());
    };

    public DigitalOceanController() {
        unirest = Unirest.spawnInstance();
        unirest.config()
                .addShutdownHook(true)
                .setDefaultHeader("Accept", "application/json")
                .setDefaultHeader("Content-Type", "application/json")
                .defaultBaseUrl("https://api.digitalocean.com/v2");
    }
}
