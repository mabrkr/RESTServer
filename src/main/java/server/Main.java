package server;

import io.javalin.Javalin;
import io.javalin.http.Context;

import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

public class Main {

    public static void main(String[] args) {

        // Setup
        Javalin app = Javalin.create(config ->
                config.addStaticFiles("/public").enableCorsForAllOrigins())
                .start(8080);

        // Fejlhåndtering
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.result("Serverfejl: " + e.toString());
        });

        // REST
        app.routes(() -> {
            before(ctx -> System.out.println(
                    "Server got " + ctx.method()
                            + " on " + ctx.url()));
            // TODO: endpoints til alt!
        });
    }
}
