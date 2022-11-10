package src.main.java.obstacles;

import src.main.java.pieces.Rocket;

public abstract class Obstacle {
    protected int width, height;                // num of blocks
    protected float moving;             // -1 -> 1 - -1 max speed left, 1 max speed right
    protected boolean isTransparent;    // rocket can fly through
    protected boolean haveAttack;

    public Obstacle(int width, int height, float moving, boolean isTransparent, boolean haveAttack){
        this.width = width;
        this.height = height;
        this.moving = moving;
        this.isTransparent = isTransparent;
        this.haveAttack = haveAttack;
    }

    public abstract void collisionEffect(Rocket rocket);
}
