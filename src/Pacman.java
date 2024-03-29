import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
/**
 * @author Emilien ANDRE
 */
public class Pacman extends Entite{
    /**
     * Le nombre de points de vies de Pacman
     */
    static int LP = 3;
    /**
     * The x direction of Pacman
     */
    byte xDir;
    /**
     * The y direction of Pacman
     */
    byte yDir;
    /**
     * The mouth state of Pacman
     * 0 for closed
     * 1 for open
     */
    byte mouthState;
    /**
     * The direction of Pacman
     * 0 is -X
     * 1 is +X
     * 2 is -Y
     * 3 is +Y
     */
    byte dir;
    /**
     * 
     * @return true if pacman's mouth is open
     */
    boolean isMouthOpen(){
        return this.mouthState == 1;
    }
    /**
     * Close Pacman's mouth by drawing another picture
     * @throws IOException
     */
    void closeMouth() throws IOException{
        this.image = ImageIO.read(new File(System.getProperty("user.dir") + "/lib/pacman_0.png"));
        this.mouthState = 0;
    }
    /**
     * Open Pacman's mouth by drawing another picture
     * @throws IOException
     */
    void openMouth() throws IOException{
        this.image = ImageIO.read(new File(System.getProperty("user.dir") + "/lib/pacman_1_" + dir + ".png"));
        this.mouthState = 1;
    }
    /**
     * Set in which direction Pacman will move
     * @param newDir "left" "right" "front" "back"
     */
    void setDir(String newDir){
        if(newDir.equals("left")){
            xDir = -1;
            yDir = 0;
            dir = 0;
        }
        else if(newDir.equals("right")){
            xDir = 1;
            yDir = 0;
            dir = 1;
        }
        else if(newDir.equals("front")){
            xDir = 0;
            yDir = -1;
            dir = 2;
        }
        else if(newDir.equals("back")){
            xDir = 0;
            yDir = 1;
            dir = 3;
        }
    }
    /**
     * -1 means -X
     * +1 means +X
     * @return the X moving direction of Pacman
     */
    byte getXDir(){
        return this.xDir;
    }
    /**
     * -1 means -Y
     * 1 means +Y
     * @return the Y moving direction of Pacman
     */
    byte getYDir(){
        return this.yDir;
    }
    /**
     * Set the X moving direction of Pacman
     * @param newXDir the new direction, -1 or 1
     */
    void setXDir(byte newXDir){
        xDir = newXDir;
    }
    /**
     * Set the Y moving direction of Pacman
     * @param newYDir
     */
    void setYDir(byte newYDir){
        yDir = newYDir;
    }
    /**
     * If Pacman is stuck in a wall his mouth remains open
     * otherwise his mouth closed if its open or open if its closed
     */
    void openCloseMouth(){
        if(isMouthOpen() && getXDir() + getYDir() != 0){//if pacman is stuck his mouth remains open
            try {
                closeMouth();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                openMouth();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Tell us whether Pacman can move or not
     * @return whether Pacman can move or not.
     */
    boolean canMove(){
        return getX() + 10 * getXDir() > 0 && getX() + 10 * getXDir() + 40 < 1920 && getY() + 10 * getYDir() > 0 && getY() + 10 * getYDir() + 40 < 1080;
    }

    /**
     * Pacman
     * 
     * @param ref la référence de pacman (=1)
     * @param etatG l'état de Pacman
     * @throws IOException
     */
    public Pacman(short ref, byte x, byte y) throws IOException{
        super(ref, x, y);
        System.out.println(System.getProperty("user.dir") + "/lib/pacman_1_0.png");
        this.mouthState = 1;
        this.xDir = -1;// move left by default
        this.yDir = 0;
        this.dir = 0;
    }

    public static int etatP;

     void move(){
        // clear the case
        App.graphics.clearRect(x*40+1, y*40+1, 39,39);
        byte[] p = getPos();
        int[][] lay0 = (DeSerializerDonnees.getLevel().getTabLayout())[0];
        Level level = DeSerializerDonnees.getLevel();
        if (yDir == 1 && y+1 < 22 && lay0[y+1][x] != 1){
            y += 1;
            level.removeEnt(x,y);
        }
        else if (xDir == 1 && x+1 < 19 &&lay0[y][x+1] != 1){
            x += 1;
            level.removeEnt(x,y);
        }
        else if (yDir == -1 && y - 1 > 0 && lay0[y-1][x] != 1){
            y -= 1;
            level.removeEnt(x,y);
        }
        else if (xDir == -1 && x - 1 > 0 && lay0[y][x-1] != 1){
            x -= 1;
            level.removeEnt(x,y);
        }
        for(int i=0; i<=3; i++){
            LPcomp.decLP(p, App.ghosts[i].getPos());
        }
        int[][] lay1 = (DeSerializerDonnees.getLevel().getTabLayout())[1];
        int PG = lay1[p[0]][p[1]];
        Compteur.incComp(PG, p, lay1);
        draw();
    }
}