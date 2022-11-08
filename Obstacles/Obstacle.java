package Obstacles;

public abstract class Obstacle {
    protected int width;                // num of blocks
    protected float moving;             // -1 -> 1 - -1 max speed left, 1 max speed right
    protected boolean isTransparent;    // rocket can fly through

    public Obstacle(int width, float moving, boolean isTransparent){
        this.width = width;
        this.moving = moving;
        this.isTransparent = isTransparent;
    }

    public abstract void effect();
}
