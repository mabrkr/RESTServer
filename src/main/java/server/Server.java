package server;

import io.javalin.Javalin;
import kong.unirest.Unirest;
import server.controllers.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Server {

    private static DigitalOceanController digitalOceanController;
    private static HetznerController hetznerController;
    private static AuthenticationController authenticationController ;
    private static UserController userController;

    public static void main(String[] args) {

        digitalOceanController = new DigitalOceanController();
        hetznerController = new HetznerController();
        authenticationController = new AuthenticationController();
        userController = new UserController();

        // Javalin server setup
        Javalin app = Javalin.create();

        app.config.enableCorsForAllOrigins();

        app.events(event -> {
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
            before("/digitalocean/*", authenticationController.authenticate);
            before("/hetzner/*", authenticationController.authenticate);
            before("/users/*", authenticationController.authenticate);

            post(Endpoints.SESSION_TOKENS, authenticationController.login);
            delete(Endpoints.SESSION_TOKEN, authenticationController.logout);

            get(Endpoints.USER, userController.getUser);
            post(Endpoints.USER_API_KEYS, userController.addApiKey);
            post(Endpoints.USERS, userController.newUser);
            post(Endpoints.USER, userController.updateUser);

            get(Endpoints.DIGITAL_OCEAN_DROPLETS, digitalOceanController.getDroplets);
            get(Endpoints.DIGITAL_OCEAN_DROPLET, digitalOceanController.getDroplet);
            get(Endpoints.DIGITAL_OCEAN_ACCOUNT, digitalOceanController.getAccountInfo);
            post(Endpoints.DIGITAL_OCEAN_DROPLETS, digitalOceanController.createDroplet);
            delete(Endpoints.DIGITAL_OCEAN_DROPLET, digitalOceanController.deleteDroplet);

            get(Endpoints.HETZNER_SERVERS, hetznerController.getServers);
            get(Endpoints.HETZNER_SERVER, hetznerController.getServer);
            post(Endpoints.HETZNER_SERVERS, hetznerController.createServer);
            delete(Endpoints.HETZNER_SERVER, hetznerController.deleteServer);
        });
    }

    // Container for endpoint URLs
    private static class Endpoints {
        private static final String SESSION_TOKENS = "/sessions";
        private static final String SESSION_TOKEN = "/sessions/:token";

        private static final String USERS = "/users";
        private static final String USER = "/users/:id";
        private static final String USER_API_KEYS = "/users/:id/apikeys";

        private static final String DIGITAL_OCEAN_DROPLETS = "/digitalocean/droplets";
        private static final String DIGITAL_OCEAN_DROPLET = "/digitalocean/droplets/:id";
        private static final String DIGITAL_OCEAN_ACCOUNT = "/digitalocean/account";

        private static final String HETZNER_SERVERS = "/hetzner/servers";
        private static final String HETZNER_SERVER = "/hetzner/servers/:id";
    }
}
