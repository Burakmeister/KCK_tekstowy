package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public class Plane extends Obstacle{
    private final float damage;
    public Plane(Arts art, int x, int y, float moving, float damage){
        super(art.art[0].length(), art.art.length, x, y, moving, false, false,5, art);
        this.damage = damage;
    }

    @Override
    public void collisionEffect(Rocket rocket) {
        float tmp = damage - ((float)(rocket.getArmor()-1)/12)*damage;
        if(tmp>0)
            rocket.setHealthStatus(rocket.getHealthStatus()-damage);
    }

    public Arts getArt() {
        return art;
    }

}
