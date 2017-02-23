import java.awt.*;
import java.awt.event.KeyEvent;

public class Player
{
    private GameKey upKey;
    private GameKey downKey;
    private int score;

    private Player()
    { /* prevent uninitialized instances */}

    public Player(GameKey up, GameKey down)
    {
        this.upKey = up;
        this.downKey = down;
        this.score = 0;
    }

    public void setKeyPressed(GameKeyType key, boolean pressed)
    {
        if (key == GameKeyType.UP)
        {
            upKey.setPressed(pressed);
        } else if (key == GameKeyType.DOWN)
        {
            downKey.setPressed(pressed);
        }
    }

    public int getKeyCode(GameKeyType key){

        if (key == GameKeyType.UP)
        {
            return upKey.getKeyCode();
        } else if (key == GameKeyType.DOWN)
        {
            return downKey.getKeyCode();
        }
        return KeyEvent.VK_EURO_SIGN; // something weird that will stand out
    }

    public boolean keyPressed(GameKeyType key){

        if (key == GameKeyType.UP)
        {
            return upKey.isPressed();
        } else if (key == GameKeyType.DOWN)
        {
            return downKey.isPressed();
        }
        return false;
    }

    public void rewardPlayer(){
        this.score ++;
        /* uncomment to make a sound
        final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
        if (runnable != null) runnable.run();
        */
    }

    public int getScore()
    {
        return score;
    }
}
