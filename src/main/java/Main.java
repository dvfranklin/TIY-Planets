import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {

        Server server = Server.createTcpServer("-baseDir", "./data").start();
        Connection connection = DriverManager.getConnection("jdbc:h2:" + server.getURL() + "/main");
        PlanetService service = new PlanetService(connection);
        service.initDatabase();


        Spark.get(
                "/planets",
                (request, response) -> {
                    Gson gson = new GsonBuilder().create();
                    ArrayList<Planet> planets = service.getPlanets();


                    return gson.toJson(planets);
                }
        );

        Spark.get(
                "/planet",
                (request, response) -> {
                    Planet planet = service.selectPlanet(Integer.valueOf(request.queryParams("id")));
                    Gson gson = new GsonBuilder().create();

                    return gson.toJson(planet);
                }
        );

        Spark.post(
                "/planet",
                (request, response) -> {
                    String planetJson = request.queryParams("planet");
                    Gson gson = new GsonBuilder().create();
                    Planet planet = gson.fromJson(planetJson, Planet.class);

                    service.insertPlanet(planet);

                    return "";
                }
        );

    }
}

