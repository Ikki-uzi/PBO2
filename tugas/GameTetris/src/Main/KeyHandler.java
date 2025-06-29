
package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    
    public static boolean upPressed, downPressed, leftPressed, rightPressed, pausePressed, mutePressed, restartPressed;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
    
            int code = e.getKeyCode();
            
            if(code == KeyEvent.VK_W){
                upPressed = true;
            }
            if(code == KeyEvent.VK_M){
               if(mutePressed){
                    mutePressed = false;
                    GamePanel.music.play(0, true);
                    GamePanel.music.loop();
                }
                else{
                    mutePressed = true;
                    GamePanel.music.stop();
                }
            }
            if(code == KeyEvent.VK_A){
                leftPressed = true;
            }
            if(code == KeyEvent.VK_S){
                downPressed = true;
            }
            if(code == KeyEvent.VK_D){
                rightPressed = true;
            }
            if(code == KeyEvent.VK_SPACE){
                if(pausePressed){
                    pausePressed = false;
                    GamePanel.music.play(0, true);
                    GamePanel.music.loop();
                }
                else{
                    pausePressed = true;
                    GamePanel.music.stop();
                }
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
