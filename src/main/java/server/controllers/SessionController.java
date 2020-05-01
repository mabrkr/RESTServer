package server.controllers;

import DAL.DatabaseController;
import io.javalin.http.Handler;

public class SessionController {

    public Handler login = ctx -> {
        /*
        * Tjek brugernavn + kodeord i database. Hvis ja: generer token og put den i database og returner den.
        * Hvis nej: returner passende fejlkode.
        */
    };

    public Handler logout = ctx -> {
        /*
        * Slet token i databasen og returner 204. Tokens skal måske udløbe automatisk efter et stykke tid?
        */
    };
}
