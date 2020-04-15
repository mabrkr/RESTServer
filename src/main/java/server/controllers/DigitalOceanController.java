package server.controllers;

import io.javalin.http.Handler;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class DigitalOceanController {
    public static Handler getDroplets = ctx -> {

    };

    public static Handler getAccount = ctx -> {
        HttpResponse<JsonNode> response = Unirest.post("https://api.digitalocean.com/v2/account")
                .header("accept", "application/json")
                .header("")
    };
}
