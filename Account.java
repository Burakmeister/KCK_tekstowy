import Pieces.Rocket;

public class Account {

    private Rocket rocket;
    private int accountBalance = 0;
    private int record;
    public Account(){
        this.rocket = Rocket.getInstance();
        this.accountBalance = 0;
        this.record = 0;
    }

    public Rocket getRocket() {
        return rocket;
    }

    public void setRocket(Rocket rocket) {
        this.rocket = rocket;
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
