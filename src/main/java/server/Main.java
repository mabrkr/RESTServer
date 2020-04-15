package server;

import io.javalin.Javalin;
import io.javalin.http.Context;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import server.controllers.DigitalOceanController;

import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

public class Main {

    public static void main(String[] args) {

        // Setup
        Javalin app = Javalin.create(config ->
                config.enableCorsForAllOrigins())
                .start(8080);

        // Fejlhåndtering TODO: lav ordentligt
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.result("Serverfejl: " + e.toString());
        });

        // REST
        app.routes(() -> {
            before(ctx -> System.out.println(
                    "Server: " + ctx.method()
                            + " on " + ctx.url()));
            // TODO: endpoints til alt!
            get(Endpoints.DIGITAL_OCEAN_DROPLETS, DigitalOceanController.getDroplets);
            get(Endpoints.DIGITAL_OCEAN_ACCOUNT, DigitalOceanController.getAccount);
        });


    }

    // Container for endpoints
    private static class Endpoints {
        private static final String DIGITAL_OCEAN_DROPLETS = "/digitalocean/droplets";
        private static final String DIGITAL_OCEAN_ACCOUNT = "/digitalocean/account";
    }
}
