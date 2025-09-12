/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

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
        // Define a porta para o Heroku. O Heroku define a porta usando a variável de ambiente PORT.
        port(getHerokuAssignedPort());

        // Rota raiz para verificar se a aplicação está no ar
        get("/", (request, response) -> {
            return "<html><body><h1>Serviço de localização iniciado com sucesso!</h1>"
                 + "<p>Use os endpoints /locations para interagir com o serviço.</p></body></html>";
        });

        // POST /locations → adiciona uma localização
        post("/locations", (request, response) -> {
            // Analisa o JSON do corpo da requisição
            Map<String, Object> locationData = gson.fromJson(request.body(), Map.class);
            locationData.put("timestamp", System.currentTimeMillis());

            // Adiciona a nova localização à lista em memória
            locations.add(locationData);

            // Retorna mensagem de sucesso em JSON
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

    // Porta dinâmica para o Heroku ou 4567 local
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}


