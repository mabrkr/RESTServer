package server.controllers;

import io.javalin.http.Handler;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;

public class DigitalOceanController {

    private UnirestInstance unirest;

    public Handler getDroplets = ctx -> {
        HttpResponse<JsonNode> response = unirest.get("/droplets")
                .header("Authorization", "Bearer 2f69ab747915383b38dd2a312aab11577c87ba12a873d3d5da8be86a07a68631")
                .asJson();

        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler getAccountInfo = ctx -> {
        HttpResponse<JsonNode> response = unirest.get("/account")
                .header("Authorization", "Bearer 2f69ab747915383b38dd2a312aab11577c87ba12a873d3d5da8be86a07a68631")
                .asJson();

        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler getDroplet = ctx -> {
        HttpResponse<JsonNode> response = unirest.get("/droplets/{id}")
                .routeParam("id", ctx.pathParam("id"))
                .header("Authorization", "Bearer 2f69ab747915383b38dd2a312aab11577c87ba12a873d3d5da8be86a07a68631")
                .asJson();

        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public Handler createDroplet = ctx -> {
//        HttpResponse<JsonNode> response = unirest.post("/droplets")
//                .header("Authorization", "Bearer 2f69ab747915383b38dd2a312aab11577c87ba12a873d3d5da8be86a07a68631")
//                .asJson();
//
//        ctx.header("Content-Type", "application/json");
//        ctx.result(response.getBody().toString());
    };

    public Handler deleteDroplet = ctx -> {
        HttpResponse<JsonNode> response = unirest.delete("/droplets/{id}")
                .routeParam("id", ctx.pathParam("id"))
                .header("Authorization", "Bearer 2f69ab747915383b38dd2a312aab11577c87ba12a873d3d5da8be86a07a68631")
                .asJson();

        ctx.header("Content-Type", "application/json");
        ctx.result(response.getStatusText());
    };

    public DigitalOceanController() {
        unirest = Unirest.spawnInstance();
        unirest.config()
                .addShutdownHook(true)
                .setDefaultHeader("accept", "application/json")
                .defaultBaseUrl("https://api.digitalocean.com/v2");
    }
}
