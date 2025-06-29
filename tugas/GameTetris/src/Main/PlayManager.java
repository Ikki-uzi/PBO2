
package Main;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;
import mino.Block;
import mino.Mino;
import mino.Mino_Bar;
import mino.Mino_L1;
import mino.Mino_L2;
import mino.Mino_Square;
import mino.Mino_T;
import mino.Mino_Z1;
import mino.Mino_Z2;

public class PlayManager {
    //Main Play Area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;
    
    //Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();
    
    //Others
    public static int dropInterval = 60; // mino drops in every 60 frames
    boolean gameOver;
    
    //Effect
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();
    
    int level = 1;
    int lines;
    int score;
    
    public PlayManager(){
        
        //Main Play Area Frame
        left_x = (GamePanel.WIDTH/2) - (WIDTH/2); // 1280/2 - 360/2 = 460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;
        
        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;
        NEXTMINO_X = right_x +175;
        NEXTMINO_Y = top_y + 500;
        
        //set the starting Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }
    private Mino pickMino(){
        // pick a random mino
        Mino mino = null;
        int i = new Random().nextInt(7);
        
        switch(i){
            case 0: mino = new Mino_L1();break;
            case 1: mino = new Mino_L2();break;
            case 2: mino = new Mino_Square();break;
            case 3: mino = new Mino_Bar();break;
            case 4: mino = new Mino_T();break;
            case 5: mino = new Mino_Z1();break;
            case 6: mino = new Mino_Z2();break;
        }
        return mino;
    }
    public void update(){
        //cek jika mino terbaru aktif
        if(currentMino.active == false) {
            
            // jika mino tidak aktif, taruh kedalam staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);
            
            //apabila gameOver
            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y){
                
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(2, false);
            }
            
            //ganti mino terkini dengan mino selanjutnya
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);
            
            currentMino.deactivating = false;
            
            //kalo minonya inactive, cek jika linenya bisa di hapus/delete
            checkDelete();
        }
        else{
          currentMino.update();  
        }
    }
    private void checkDelete(){
      int x = left_x;
      int y = top_y;
      int blockCount = 0;
      int lineCount = 0;
     
      while(x < right_x && y < bottom_y){
          
            for(int i = 0; i <staticBlocks.size(); i++){
              if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                  
                  blockCount++;
              }
          }

            
          x += Block.SIZE;
          
          if(x == right_x){
              
              if(blockCount == 12){
                  
                  effectCounterOn = true;
                  effectY.add(y);
                  for(int i = staticBlocks.size()-1; i > -1; i--) {
                      //hapus semua block yang ada di line y
                      if(staticBlocks.get(i).y == y) {
                          staticBlocks.remove(i);
                      }
                  }
                  lineCount++;
                  lines++;
                  //Drop speed
                  //jika skor menyentuh angka tertentu, naikkan kecepatan turunnya
                  if (lines % 10 == 0 && dropInterval > 1){
                      
                      level++;
                      if(dropInterval > 10){
                          dropInterval -= 10;
                      }
                      else {
                          dropInterval -= 10;
                      }
                  }
                  
                              
            //jika line sudah dihapus, turunkan block yang ada di atasnya
            for(int i = 0; i < staticBlocks.size(); i++){
                if(staticBlocks.get(i).y < y){
                    staticBlocks.get(i).y += Block.SIZE;
                }
            }
                  
              }
              blockCount = 0;
              x = left_x;
              y += Block.SIZE;
          }
      }
      //add score
      if(lineCount > 0){
          GamePanel.se.play(1, false);
          int singleLineScore = 10 * level;
          score += singleLineScore * lineCount;
      }
    }
    public void draw(Graphics2D g2){
        
        //Draw Play Area Frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);
        
        //Draw Next Mino Frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60, y+60);
        
        //draw score frame
        g2.drawRect(x ,top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y); y+=70;
        g2.drawString("LINES: " + lines, x, y); y+=70;
        g2.drawString("SCORE: " + score, x, y);
        
        //Draw the currentMino
        if(currentMino != null) {
            currentMino.draw(g2);
        }    
        //Draw the nextMino
            nextMino.draw(g2);
            
        //draw Static Blocks
            for(int i = 0; i < staticBlocks.size(); i++){
                staticBlocks.get(i).draw(g2);
            }
            
            //Draw effect
            if(effectCounterOn){
                effectCounter++;
                
                g2.setColor(Color.red);
                for(int i = 0; i < effectY.size(); i++){
                    g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
                }
                if(effectCounter == 10) {
                    effectCounterOn = false;
                    effectCounter = 0;
                    effectY.clear();
                }
            }
        //Draw pause or GameOver
        g2.setColor(Color.red);
        g2.setFont(g2.getFont().deriveFont(50f));
        if(gameOver) {
            x = left_x + 25;
            y = top_y + 320;
            g2.setFont(new Font("Impact", Font.BOLD, 60));
            g2.drawString("GAME OVER", x, y);
        }
        else if(KeyHandler.pausePressed){
            x = left_x + 70;
            y = top_y + 320;
            g2.setFont(new Font("Impact", Font.BOLD, 60));
            g2.drawString("PAUSED", x, y);
            }
        
        //Draw Game Title
        x = 150;
        y = top_y +320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Impact", Font.ITALIC, 60));
        g2.drawString("TETRIS", x, y);
        }
    }

