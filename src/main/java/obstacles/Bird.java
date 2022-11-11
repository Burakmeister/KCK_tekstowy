package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public class Bird extends Obstacle{
    private float damage = 0.1f;
    public Bird(float moving, int x, int y){
        super(Arts.BIRD.art[0].length(), Arts.BIRD.art.length, x, y, moving, false, false, 1, Arts.BIRD);
    }

    @Override
    public void collisionEffect(Rocket rocket) {
        float tmp = damage - (float)(rocket.getArmor())/5;
        if(tmp>0)
            rocket.setHealthStatus(rocket.getHealthStatus()-damage);
    }
}
