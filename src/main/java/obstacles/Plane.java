package src.main.java.obstacles;

import src.main.java.pieces.Rocket;

public class Plane extends Obstacle{
    private float damage;
    public Plane(int width, int height, float moving, float damage){
        super(width, height, moving, false, false);
        this.damage = damage;
    }

    @Override
    public void collisionEffect(Rocket rocket) {
        rocket.setHealthStatus(rocket.getHealthStatus()-damage);
    }
}
