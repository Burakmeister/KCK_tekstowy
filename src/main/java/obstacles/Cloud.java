package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public class Cloud extends Obstacle{
    public Cloud(Arts art){
        super(art.art[0].length(), art.art.length, 0, true, false);
    }

    @Override
    public void collisionEffect(Rocket rocket) {
        rocket.setMapVisible(false);
    }
}
