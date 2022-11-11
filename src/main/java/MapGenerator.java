package src.main.java;

import com.googlecode.lanterna.graphics.TextGraphics;
import src.main.java.obstacles.*;
import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

import java.awt.font.GlyphMetrics;
import java.util.ArrayList;
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

    public void newMap(){
        this.n = 0;
        this.map = new String[Game.MAX_HEIGHT];
        String temp = null;
        Random rand = new Random();
        int i=0;
        for(i=0 ; i<Game.MAX_HEIGHT; i++)
        {
            temp = "";
            for(int j=0 ; j<Menu.frameWidth; j++){
                int randNum = rand.nextInt(10000);
                switch (randNum){
                    case 500, 501, 502, 503, 504, 505, 506, 507, 508:{
                        if(i>=Menu.frameHeight)
                        {
                            if(randNum == 500 || randNum == 501 || randNum == 506)
                                Game.getObstacles().add(new Cloud(Arts.CLOUD1, j,i));
                            else if(randNum == 502 || randNum == 503 || randNum == 507)
                                Game.getObstacles().add(new Cloud(Arts.CLOUD2, j,i));
                            else{
                                Game.getObstacles().add(new Cloud(Arts.CLOUD3, j,i));
                            }
                        }
                        temp+=' ';
                        break;
                    }
                    case 700, 701, 702, 703, 704:{
                        if(i>=Menu.frameHeight)
                        {
                            int r = rand.nextInt(4);
                            if(r==0){
                                Game.getObstacles().add(new Plane(Arts.PLANE, j, i, rand.nextFloat(1)-1, 0.35f));
                            }else if(r==1){
                                Game.getObstacles().add(new Plane(Arts.HELICOPTER, j, i, rand.nextFloat(1), 0.25f));
                            }else if(r==2){
                                Game.getObstacles().add(new Plane(Arts.FAST_PLANE, j, i, rand.nextFloat(1), 0.5f));
                            }else{
                                Game.getObstacles().add(new Plane(Arts.AEROSLAT, j, i, rand.nextFloat(1)-1, 0.2f));
                            }
                        }
                        temp+=' ';
                        break;
                    }
                    case 600, 601, 602, 603:{
                        if(i>=Menu.frameHeight)
                        {
                            Game.getObstacles().add(new Ufo(j,i, rand.nextFloat(1)-0.5f, 0.7f));
                        }
                        temp+=' ';
                        break;
                    }
                    case 1600, 9000, 900, 901, 902, 903:{
                        if(i>=Menu.frameHeight)
                        {
                            Game.getObstacles().add(new Bird(rand.nextFloat(2)-1, j, i));
                        }
                        temp+=' ';
                        break;
                    }
                    default:{
                        if(randNum<70){
                            temp+='*';
                        }else
                            temp+=' ';
                        continue;
                    }
                }
            }
            this.map[i] = temp;
        }
        for(i=0 ; i<Menu.frameHeight; i++){
            this.frame[i]=this.map[i];
        }
    }

    public String[] getFrame(TextGraphics tg){
        this.n++;
        for(int i=n ; i<Menu.frameHeight+n; i++){
            this.frame[i-n] = this.map[i];
        }
        return this.frame;
    }

    public String []getCurFrame(){
        return this.frame;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public static boolean checkColisions(Rocket rocket, Obstacle obstacle){
        if(     !obstacle.isTransparent() &&
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
