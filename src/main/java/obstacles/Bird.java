package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public class Bird extends Obstacle{
    public Bird(float moving, int x, int y){
        super(Arts.BIRD.art[0].length(), Arts.BIRD.art.length, x, y, moving, false, false, 1, Arts.BIRD);
    }

    @Override
    public void collisionEffect(Rocket rocket) {
        float damage = 0.1f;
        float tmp = damage - ((float)(rocket.getArmor()-1)/12)* damage;
        if(tmp>0)
            rocket.setHealthStatus(rocket.getHealthStatus()- damage);
    }
}
