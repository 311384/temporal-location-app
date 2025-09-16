package com.gemini.app.temporallocation;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;

public class TemporalLocationService {

    private static List<Map<String, Object>> locations = new ArrayList<>();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        // Define a porta (Heroku ou local)
        port(getHerokuAssignedPort());

        // Informa ao Spark onde estão os arquivos estáticos (HTML, CSS, JS)
        staticFiles.location("/public");

        // Rota raiz → redireciona para o index.html
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        // POST /locations → adiciona uma localização
        post("/locations", (request, response) -> {
            Map<String, Object> locationData = gson.fromJson(request.body(), Map.class);
            locationData.put("timestamp", System.currentTimeMillis());

            locations.add(locationData);

            response.type("application/json");
            Map<String, String> result = new HashMap<>();
            result.put("message", "Location recorded successfully");
            return gson.toJson(result);
        });

        // GET /locations → lista todas as localizações
        get("/locations", (request, response) -> {
            response.type("application/json");
            return gson.toJson(locations);
        });

        System.out.println("Serviço de localização iniciado...");
    }

    // Porta dinâmica para Heroku ou 4567 local
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}
