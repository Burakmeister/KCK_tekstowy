package src.main.java;

import src.main.java.obstacles.*;
import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

import java.util.Random;

public class MapGenerator {

    private int n;
    private final String[] frame = new String[Menu.frameHeight];
    private String[] map;
    private static final MapGenerator mapGenerator = new MapGenerator();

    private MapGenerator(){
    }

    public static MapGenerator getInstance() {
        return mapGenerator;
    }

    public void newMap(){
        this.n = 0;
        this.map = new String[Game.MAX_HEIGHT];
        StringBuilder temp;
        Random rand = new Random();
        int i;
        for(i=0 ; i<Game.MAX_HEIGHT; i++)
        {
            temp = new StringBuilder();
            for(int j=0 ; j<Menu.frameWidth; j++){
                int randNum = rand.nextInt(10000);
                switch (randNum) {
                    case 500, 501, 502, 503, 504, 505, 506, 507, 508 -> {
                        if (i >= 1.5*Menu.frameHeight) {
                            if (randNum == 500 || randNum == 501 || randNum == 506)
                                Game.getObstacles().add(new Cloud(Arts.CLOUD1, j, i));
                            else if (randNum == 502 || randNum == 503 || randNum == 507)
                                Game.getObstacles().add(new Cloud(Arts.CLOUD2, j, i));
                            else {
                                Game.getObstacles().add(new Cloud(Arts.CLOUD3, j, i));
                            }
                        }
                        temp.append(' ');
                    }
                    case 700, 701, 702, 703, 704 -> {
                        if (i >= 1.5*Menu.frameHeight) {
                            int r = rand.nextInt(4);
                            if (r == 0) {
                                Game.getObstacles().add(new Plane(Arts.PLANE, j, i, rand.nextFloat(1) - 1, 0.35f));
                            } else if (r == 1) {
                                Game.getObstacles().add(new Plane(Arts.HELICOPTER, j, i, rand.nextFloat(1), 0.25f));
                            } else if (r == 2) {
                                Game.getObstacles().add(new Plane(Arts.FAST_PLANE, j, i, rand.nextFloat(1), 0.5f));
                            } else {
                                Game.getObstacles().add(new Plane(Arts.AEROSLAT, j, i, rand.nextFloat(1) - 1, 0.2f));
                            }
                        }
                        temp.append(' ');
                    }
                    case 600, 601, 602, 603 -> {
                        if (i >= 1.5*Menu.frameHeight) {
                            Game.getObstacles().add(new Ufo(j, i, rand.nextFloat(1) - 0.5f, 0.7f));
                        }
                        temp.append(' ');
                    }
                    case 1600, 9000, 900, 901, 902, 903 -> {
                        if (i >= 1.5*Menu.frameHeight) {
                            Game.getObstacles().add(new Bird(rand.nextFloat(2) - 1, j, i));
                        }
                        temp.append(' ');
                    }
                    default -> {
                        if (randNum < 70) {
                            temp.append('*');
                        } else
                            temp.append(' ');
                    }
                }
            }
            this.map[i] = temp.toString();
        }
        for(i=0 ; i<Menu.frameHeight; i++){
            this.frame[i]=this.map[i];
        }
    }

    public String[] getFrame(){
        this.n++;
        System.arraycopy(this.map, n, this.frame, 0, Menu.frameHeight + n - n);
        return this.frame;
    }

    public String []getCurFrame(){
        return this.frame;
    }

    public int getN() {
        return n;
    }

    public static boolean checkColisions(Rocket rocket, Obstacle obstacle){
        if(     obstacle.isTransparent() &&
                ((rocket.getColumn()<(int)obstacle.getX()+obstacle.getArt().art[0].length()
                && rocket.getColumn()+Arts.ROCKET_FAST.art[0].length()> (int)obstacle.getX())
                ||
                (rocket.getColumn()>(int)obstacle.getX()+obstacle.getArt().art[0].length()
                && rocket.getColumn()+Arts.ROCKET_FAST.art[0].length()<(int)obstacle.getX())
                ||
                (rocket.getColumn() >= (int)obstacle.getX()
                        && rocket.getColumn()+Arts.ROCKET_FAST.art[0].length() <= (int)obstacle.getX()+obstacle.getArt().art[0].length()))
                &&
                obstacle.getY() > Menu.frameHeight-Arts.ROCKET_FAST.art.length-obstacle.getArt().art.length
                ){
            obstacle.collisionEffect(rocket);
            return true;
        }
        return false;
    }
}
