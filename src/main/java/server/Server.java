package server;

import DAO.DatabaseConnection;
import io.javalin.Javalin;
import kong.unirest.Unirest;
import server.controllers.DigitalOceanController;
import server.controllers.HetznerController;
import server.controllers.SessionController;
import server.controllers.UserController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Server {

    private static DigitalOceanController digitalOceanController = new DigitalOceanController();
    private static HetznerController hetznerController = new HetznerController();
    private static SessionController sessionController = new SessionController();
    private static UserController userController = new UserController();

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
            post(Endpoints.SESSION_TOKENS, sessionController.login);
            delete(Endpoints.SESSION_TOKENS, sessionController.logout);

            post(Endpoints.USERS, userController.newUser);
            put(Endpoints.USER, userController.updateUser);

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

        private static final String USERS = "/users";
        private static final String USER = "/users/:id";

        private static final String DIGITAL_OCEAN_DROPLETS = "/digitalocean/droplets";
        private static final String DIGITAL_OCEAN_DROPLET = "/digitalocean/droplets/:id";
        private static final String DIGITAL_OCEAN_ACCOUNT = "/digitalocean/account";

        private static final String HETZNER_SERVERS = "/hetzner/servers";
        private static final String HETZNER_SERVER = "/hetzner/servers/:id";
    }
}
