package server;

import io.javalin.Javalin;
import kong.unirest.Unirest;
import server.controllers.DigitalOceanController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Server {

    private static DigitalOceanController digitalOceanController = new DigitalOceanController();

    public static void main(String[] args) {

        // Javalin server setup
        Javalin app = Javalin.create(config ->
                config.enableCorsForAllOrigins())
                .events(event -> {
                    event.serverStopped(() -> Unirest.shutDown());
                });

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.result("Server error: " + e.toString());
        });

        app.start(8080);

        // Javalin REST endpoints
        app.routes(() -> {
            before(ctx -> System.out.println("Server: " + ctx.method() + " on " + ctx.url()));
            // TODO: endpoints til alt!
            get(Endpoints.DIGITAL_OCEAN_DROPLETS, digitalOceanController.getDroplets);
            get(Endpoints.DIGITAL_OCEAN_DROPLET, digitalOceanController.getDroplet);
            get(Endpoints.DIGITAL_OCEAN_ACCOUNT, digitalOceanController.getAccountInfo);
            post(Endpoints.DIGITAL_OCEAN_DROPLETS, digitalOceanController.createDroplet);
            delete(Endpoints.DIGITAL_OCEAN_DROPLET, digitalOceanController.deleteDroplet);
        });
    }

    // Container for endpoint URLs
    private static class Endpoints {
        private static final String DIGITAL_OCEAN_DROPLETS = "/digitalocean/droplets";
        private static final String DIGITAL_OCEAN_DROPLET = "/digitalocean/droplets/:id";
        private static final String DIGITAL_OCEAN_ACCOUNT = "/digitalocean/account";
    }
}
