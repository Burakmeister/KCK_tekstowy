package src.main.java.pieces;

public class Rocket{
    private static Rocket rocket = new Rocket();

    private int accountBalance = 0;
    private int record = 0;
    private int column = 0;

    private int speed = 0;
    private float oilStatus = 1;
    private float height = 0;
    private float healthStatus = 1;

    private int tankCapacity=1, firepower=1, armor=1, rateOfFire=1;

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
        if(getAccountBalance()>=this.tankCapacity*100 && this.tankCapacity<10){
            setAccountBalance(getAccountBalance()-this.tankCapacity*100);
            this.tankCapacity++;
        }
    }
    public void plusOneFirepower(){
        if(getAccountBalance()>=this.firepower*100 && this.firepower<10){
            setAccountBalance(getAccountBalance()-this.firepower*100);
            this.firepower++;
        }
    }
    public void plusOneArmor(){
        if(getAccountBalance()>=this.armor*100 && this.armor<10){
            setAccountBalance(getAccountBalance()-this.armor*100);
            this.armor++;
        }
    }
    public void plusOneRateOfFire(){
        if(getAccountBalance()>=this.rateOfFire*100 && this.rateOfFire<10){
            setAccountBalance(getAccountBalance()-this.rateOfFire*100);
            this.rateOfFire++;
        }
    }

    public float getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(float healthStatus) {
        this.healthStatus = healthStatus;
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
    public static void newInstance(){
        Rocket.rocket = new Rocket();
    }
}
