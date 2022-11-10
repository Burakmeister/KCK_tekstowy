package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public class Bird extends Obstacle{
    private float damage = 0.1f;
    public Bird(float moving){
        super(Arts.BIRD.art[0].length(), Arts.BIRD.art.length, moving, false, false);
    }

    @Override
    public void collisionEffect(Rocket rocket) {
        rocket.setHealthStatus(rocket.getHealthStatus()-damage);
    }
}
