import java.util.Random;

public class Player extends GamePanel{

    int posX;
    int posY;
    Random random = new Random();
    
    Player(){
        posX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        posY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public int getX(){
        return posX;
    }

    public int getY(){
        return posY;
    }

    public void setX(int x){
        this.posX = x;
    }

    public void setY(int y){
        this.posY = y;
    }

}
