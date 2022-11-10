package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public class Ufo extends Obstacle{
    private float damage;
    public Ufo(int x, int y, float moving, float damage){
        super(Arts.UFO.art[0].length(), Arts.UFO.art.length, x, y, moving, false, false);
        this.damage = damage;
    }
    @Override
    public void collisionEffect(Rocket rocket) {
        rocket.setHealthStatus(rocket.getHealthStatus()-damage);
    }
}
