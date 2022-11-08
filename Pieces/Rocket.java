package Pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rocket implements Serializable {
    final private static Rocket rocket = new Rocket();


    private List<Part> parts = new ArrayList<>();
    private int speed = 1;
    private int column;
    private int vector = 1;   // 1-prosto,  2,3,4-prawo  5,6,7-prawo_dol  8-dol  9,10,11-prawo_dol  12,13,14-lewo_prosto
    private float oilStatus = 1;
    private float height = 0;

    private Rocket(){
    }

    public static Rocket getInstance(){
        return rocket;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public float getOilStatus() {
        return oilStatus;
    }

    public void setOilStatus(float oilStatus) {
        this.oilStatus = oilStatus;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
