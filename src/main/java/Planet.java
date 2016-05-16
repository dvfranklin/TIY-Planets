import java.util.ArrayList;

public class Planet {

    int id;
    String name;
    int radius;
    boolean supportsLife;
    double distanceFromSun;

    ArrayList<Moon> moons = new ArrayList<>();

    public Planet(String name, int radius, boolean supportsLife, double distanceFromSun) {
        this.name = name;
        this.radius = radius;
        this.supportsLife = supportsLife;
        this.distanceFromSun = distanceFromSun;
    }

    public Planet(String name, int radius, boolean supportsLife, double distanceFromSun, ArrayList<Moon> moons) {
        this.name = name;
        this.radius = radius;
        this.supportsLife = supportsLife;
        this.distanceFromSun = distanceFromSun;
        this.moons = moons;
    }

    public Planet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isSupportsLife() {
        return supportsLife;
    }

    public void setSupportsLife(boolean supportsLife) {
        this.supportsLife = supportsLife;
    }

    public double getDistanceFromSun() {
        return distanceFromSun;
    }

    public void setDistanceFromSun(double distanceFromSun) {
        this.distanceFromSun = distanceFromSun;
    }

    public ArrayList<Moon> getMoons() {
        return moons;
    }

    public void setMoons(ArrayList<Moon> moons) {
        this.moons = moons;
    }
}
