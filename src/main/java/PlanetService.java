import java.sql.*;
import java.util.ArrayList;

public class PlanetService {

    private final Connection connection;

    public PlanetService(Connection connection){
        this.connection = connection;
    }

    public void initDatabase() throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.execute("CREATE TABLE IF NOT EXISTS planet (id IDENTITY, name VARCHAR, radius INT, supports_life BOOLEAN, distance_from_sun DOUBLE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS moon (id IDENTITY, name VARCHAR, color VARCHAR, planet_id INT)");
    }

    public void insertPlanet(Planet planet) throws SQLException {
        PreparedStatement prepStmt = connection.prepareStatement("INSERT INTO planet VALUES (NULL, ?, ?, ?, ?)");
        prepStmt.setString(1, planet.name);
        prepStmt.setInt(2, planet.radius);
        prepStmt.setBoolean(3, planet.supportsLife);
        prepStmt.setDouble(4, planet.distanceFromSun);


        prepStmt.execute();

        ResultSet results = prepStmt.getGeneratedKeys();
        results.next();
        planet.setId(results.getInt(1));
        System.out.println(planet.getId());

        for(Moon moon : planet.moons){
            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO moon VALUES (NULL, ?, ?, ?)");
            ps2.setString(1, moon.name);
            ps2.setString(2, moon.color);
            ps2.setInt(3, planet.getId());
            ps2.execute();

            ResultSet rs = ps2.getGeneratedKeys();
            rs.next();
            moon.setId(rs.getInt(1));
        }
    }

    public Planet selectPlanet(int planetId) throws SQLException {
        PreparedStatement prepStmt = connection.prepareStatement("SELECT * FROM planet LEFT JOIN moon ON planet.id = moon.planet_id WHERE planet.id = ?");
        prepStmt.setInt(1, planetId);
        Planet planet = new Planet();

        ResultSet rs = prepStmt.executeQuery();
        while (rs.next()){
            planet.setId(rs.getInt("id"));
            planet.setName(rs.getString("name"));
            planet.setRadius(rs.getInt("radius"));
            planet.setSupportsLife(rs.getBoolean("supports_life"));
            planet.setDistanceFromSun(rs.getDouble("distance_from_sun"));

            Moon moon = new Moon(rs.getInt("moon.id"), rs.getString("moon.name"), rs.getString("moon.color"));
            planet.moons.add(moon);
        }

        return planet;
    }

    public ArrayList<Planet> getPlanets() throws SQLException {
        PreparedStatement prepStmt = connection.prepareStatement("SELECT * FROM planet AS p\n" +
                "LEFT OUTER JOIN moon AS m\n" +
                "ON p.id = m.planet_id");
        ArrayList<Planet> planets = new ArrayList<>();
        Planet lastPlanet = new Planet();

        ResultSet rs = prepStmt.executeQuery();

        while (rs.next()) {

            if(rs.getInt("id") != lastPlanet.getId()){

                if(lastPlanet.getId() != 0) planets.add(lastPlanet);
                Planet planet = new Planet();
                planet.setId(rs.getInt("id"));
                planet.setName(rs.getString("name"));
                planet.setRadius(rs.getInt("radius"));
                planet.setSupportsLife(rs.getBoolean("supports_life"));
                planet.setDistanceFromSun(rs.getDouble("distance_from_sun"));

                Moon moon = new Moon();
                moon.setId(rs.getInt("moon.id"));
                moon.setName(rs.getString("moon.name"));
                moon.setColor(rs.getString("moon.color"));
                if(moon.getId() != 0) planet.moons.add(moon);
                lastPlanet = planet;

            } else{
                Moon moon = new Moon();
                moon.setId(rs.getInt("moon.id"));
                moon.setName(rs.getString("moon.name"));
                moon.setColor(rs.getString("moon.color"));
                lastPlanet.moons.add(moon);

            }

        }

        planets.add(lastPlanet);

        return planets;
    }


}

