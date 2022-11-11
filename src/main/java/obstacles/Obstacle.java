package src.main.java.obstacles;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

public abstract class Obstacle {
    protected int healthPoints;
    protected int width, height;                // num of blocks
    protected int y;
    protected float x;
    protected float moving;             // -1 -> 1 - -1 max speed left, 1 max speed right
    protected boolean isTransparent;    // rocket can fly through
    protected boolean haveAttack;
    private boolean isENED = false;
    private boolean toRemove = false;
    protected Arts art;

    public Obstacle(int width, int height, int x, int y, float moving, boolean isTransparent, boolean haveAttack, int healthPoints, Arts art){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.moving = moving;
        this.isTransparent = isTransparent;
        this.haveAttack = haveAttack;
        this.healthPoints = healthPoints;
        this.art = art;
    }

    public abstract void collisionEffect(Rocket rocket);

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getMoving() {
        return moving;
    }

    public boolean isTransparent() {
        return !isTransparent;
    }

    public Boolean isENED(){
        return isENED;
    }

    public void n(){
        isENED=true;
    }

    public Boolean isToRemove(){
        return this.toRemove;
    }

    public void setToRemove(){
        this.toRemove = true;
    }

    public Arts getArt() {
        return art;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
}
