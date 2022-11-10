package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public class Plane extends Obstacle{
    private float damage;
    private Arts art;
    public Plane(Arts art, int x, int y, float moving, float damage){
        super(art.art[0].length(), art.art.length, x, y, moving, false, false);
        this.damage = damage;
        this.art = art;
    }

    @Override
    public void collisionEffect(Rocket rocket) {
        rocket.setHealthStatus(rocket.getHealthStatus()-damage);
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Arts getArt() {
        return art;
    }

    public void setArt(Arts art) {
        this.art = art;
    }
}
