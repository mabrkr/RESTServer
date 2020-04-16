package server.controllers;

import io.javalin.http.Handler;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class DigitalOceanController {
    public static Handler getDroplets = ctx -> {
        HttpResponse<JsonNode> response = Unirest.get("https://api.digitalocean.com/v2/droplets")
                .header("accept", "application/json")
                .header("Authorization", "Bearer 2f69ab747915383b38dd2a312aab11577c87ba12a873d3d5da8be86a07a68631")
                .asJson();

        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };

    public static Handler getAccount = ctx -> {
        HttpResponse<JsonNode> response = Unirest.get("https://api.digitalocean.com/v2/account")
                .header("accept", "application/json")
                .header("Authorization", "Bearer 2f69ab747915383b38dd2a312aab11577c87ba12a873d3d5da8be86a07a68631")
                .asJson();

        ctx.header("Content-Type", "application/json");
        ctx.result(response.getBody().toString());
    };
    public static Handler getDroplet;
    public static Handler createDroplet;
    public static Handler deleteDroplet;
}
