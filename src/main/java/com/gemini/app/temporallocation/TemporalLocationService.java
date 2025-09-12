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

        // Define a rota POST para adicionar uma nova localização.
        // O cliente envia um JSON com latitude, longitude e timestamp.
        post("/locations", (request, response) -> {
            // Analisa o JSON do corpo da requisição
            Map<String, Object> locationData = gson.fromJson(request.body(), Map.class);
            locationData.put("timestamp", System.currentTimeMillis());
            
            // Adiciona a nova localização à lista em memória
            locations.add(locationData);
            
            // Define o tipo de conteúdo da resposta como JSON
            response.type("application/json");
            // Retorna uma mensagem de sucesso
            return gson.toJson(Map.of("message", "Location recorded successfully"));
        });

        // Define a rota GET para listar todas as localizações.
        get("/locations", (request, response) -> {
            response.type("application/json");
            // Retorna a lista de localizações como JSON
            return gson.toJson(locations);
        });

        System.out.println("Serviço de localização iniciado...");
    }

    /**
     * Obtém a porta atribuída pelo Heroku, ou usa a porta 4567 para desenvolvimento local.
     * @return A porta do servidor.
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // Porta para desenvolvimento local
    }
}

