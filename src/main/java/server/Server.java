package server;

import io.javalin.Javalin;
import kong.unirest.Unirest;
import server.controllers.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Server {

    private static DigitalOceanController digitalOceanController;
    private static HetznerController hetznerController;
    private static AuthorizationController authorizationController;
    private static UserController userController;

    public static void main(String[] args) {

        digitalOceanController = new DigitalOceanController();
        hetznerController = new HetznerController();
        authorizationController = new AuthorizationController();
        userController = new UserController();


        Javalin app = Javalin.create(config -> {
                config.enableCorsForAllOrigins();
        }).start(8080);

        app.events(event -> {
            event.serverStopped(() -> Unirest.shutDown());
        });

        // Javalin REST endpoints
        app.routes(() -> {
            before(ctx -> System.out.println("Server: " + ctx.method() + " on " + ctx.url()));

            post(Endpoints.SESSION_TOKENS, authorizationController.login);
            delete(Endpoints.SESSION_TOKEN, authorizationController.logout);

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
        private static final String USER = "/users/:username";
        private static final String USER_API_KEYS = "/users/:username/apikeys";

        private static final String DIGITAL_OCEAN_DROPLETS = "/digitalocean/droplets";
        private static final String DIGITAL_OCEAN_DROPLET = "/digitalocean/droplets/:id";
        private static final String DIGITAL_OCEAN_ACCOUNT = "/digitalocean/account";

        private static final String HETZNER_SERVERS = "/hetzner/servers";
        private static final String HETZNER_SERVER = "/hetzner/servers/:id";
    }
}
