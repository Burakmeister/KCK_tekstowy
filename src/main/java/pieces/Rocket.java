package src.main.java.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rocket implements Serializable {
    final private static Rocket rocket = new Rocket();

    private int accountBalance = 0;
    private int record;
    private List<Part> parts = new ArrayList<>();
    private int column = 0;

    private int speed = 1;
    private float oilStatus = 1;
    private float height = 0;
    private float healthStatus = 1;

    private Boolean mapIsVisible = true;

//    stats
    private int tankCapacity=1, firepower=1, armor=1, rateOfFire=1, repairKitEfficacy=1;


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
    public void plusOneTankCapacity(){
        if(getAccountBalance()>=this.tankCapacity*100 && this.tankCapacity<18){
            setAccountBalance(getAccountBalance()-this.tankCapacity*100);
            this.tankCapacity++;
        }
    }
    public void plusOneFirepower(){
        if(getAccountBalance()>=this.firepower*100 && this.firepower<18){
            setAccountBalance(getAccountBalance()-this.firepower*100);
            this.firepower++;
        }
    }
    public void plusOneArmor(){
        if(getAccountBalance()>=this.armor*100 && this.armor<18){
            setAccountBalance(getAccountBalance()-this.armor*100);
            this.armor++;
        }
    }
    public void plusOneRateOfFire(){
        if(getAccountBalance()>=this.rateOfFire*100 && this.rateOfFire<18){
            setAccountBalance(getAccountBalance()-this.rateOfFire*100);
            this.rateOfFire++;
        }
    }
    public void plusOneRepairKitEfficacy(){
        if(getAccountBalance()>=this.repairKitEfficacy*100 && this.repairKitEfficacy<18){
            setAccountBalance(getAccountBalance()-this.repairKitEfficacy*100);
            this.repairKitEfficacy++;
        }
    }

    public float getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(float healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Boolean mapIsVisible() {
        return mapIsVisible;
    }

    public void setMapVisible(Boolean mapIsVisible) {
        this.mapIsVisible = mapIsVisible;
    }

    public Boolean getMapIsVisible() {
        return mapIsVisible;
    }

    public int getTankCapacity() {
        return tankCapacity;
    }

    public int getFirepower() {
        return firepower;
    }

    public int getArmor() {
        return armor;
    }

    public int getRateOfFire() {
        return rateOfFire;
    }

    public int getRepairKitEfficacy() {
        return repairKitEfficacy;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }
}
