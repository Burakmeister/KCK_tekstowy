package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public class Ufo extends Obstacle{
    private float damage;
    public Ufo(float moving, float damage){
        super(Arts.UFO.art[0].length(), Arts.UFO.art.length, moving, false, false);
        this.damage = damage;
    }
    @Override
    public void collisionEffect(Rocket rocket) {
        rocket.setHealthStatus(rocket.getHealthStatus()-damage);
    }
}
