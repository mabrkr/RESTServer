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
import static io.javalin.apibuilder.ApiBuilder.delete;

public class Main {

    public static void main(String[] args) {

        // Initial setup
        Javalin app = Javalin.create(config ->
                config.enableCorsForAllOrigins())
                .start(8080);

        // Error handling TODO: lav ordentligt
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
            get(Endpoints.DIGITAL_OCEAN_DROPLET, DigitalOceanController.getDroplet);
            get(Endpoints.DIGITAL_OCEAN_ACCOUNT, DigitalOceanController.getAccount);
            post(Endpoints.DIGITAL_OCEAN_DROPLETS, DigitalOceanController.createDroplet);
            delete(Endpoints.DIGITAL_OCEAN_DROPLET, DigitalOceanController.deleteDroplet);
        });


    }

    // Container for endpoint URLs
    private static class Endpoints {
        private static final String DIGITAL_OCEAN_DROPLETS = "/digitalocean/droplets";
        private static final String DIGITAL_OCEAN_DROPLET = "/digitalocean/droplets/:id";
        private static final String DIGITAL_OCEAN_ACCOUNT = "/digitalocean/account";
    }
}
