package src.main.java.obstacles;

import src.main.java.pieces.Rocket;

public abstract class Obstacle {
    protected int width, height;                // num of blocks
    protected int x, y;
    protected float moving;             // -1 -> 1 - -1 max speed left, 1 max speed right
    protected boolean isTransparent;    // rocket can fly through
    protected boolean haveAttack;
    private boolean isENED = false;
    private boolean toRemove = false;

    public Obstacle(int width, int height, int x, int y, float moving, boolean isTransparent, boolean haveAttack){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.moving = moving;
        this.isTransparent = isTransparent;
        this.haveAttack = haveAttack;
    }

    public abstract void collisionEffect(Rocket rocket);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getMoving() {
        return moving;
    }

    public void setMoving(float moving) {
        this.moving = moving;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    public boolean isHaveAttack() {
        return haveAttack;
    }

    public void setHaveAttack(boolean haveAttack) {
        this.haveAttack = haveAttack;
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
}
