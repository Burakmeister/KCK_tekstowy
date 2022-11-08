import java.util.Random;

public class MapGenerator {

    private int n;
    private String[] frame = new String[Menu.frameHeight];
    private String[] map;
    private static MapGenerator mapGenerator = new MapGenerator();

    private MapGenerator(){
    }

    public static MapGenerator getInstance() {
        return mapGenerator;
    }

    public String[] newMap(){
        this.n = 0;
        this.map = new String[Game.MAX_HEIGHT];
        String temp = null;
        Random rand = new Random();
        int i=0;
        for(i=0 ; i<Game.MAX_HEIGHT; i++)
        {
            temp = "";
            for(int j=0 ; j<Menu.frameWidth-Menu.frameWidthMenu; j++){
                int randNum = rand.nextInt(1000);
                switch (randNum){
                    case 0,1,2,3,4:{
                        temp+='*';
                        continue;
                    }
                    default:{
                        temp+=' ';
                        continue;
                    }
                }
            }
            this.map[i] = temp;
        }
        return this.map;
    }

    public String[] getFrame(){
        this.n++;
        for(int i=n ; i<Menu.frameHeight+n; i++){
            this.frame[i-n] = this.map[i];
        }
        return this.frame;
    }

    public int getN() {
        return n;
    }
}
