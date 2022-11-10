package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public class Cloud extends Obstacle{
    Arts art;
    public Cloud(Arts art, int x, int y){
        super(art.art[0].length(), art.art.length, x, y, 0, true, false);
        this.art = art;
    }

    @Override
    public void collisionEffect(Rocket rocket) {
        rocket.setMapVisible(false);
    }

    public Arts getArt() {
        return art;
    }

    public void setArt(Arts art) {
        this.art = art;
    }
}
