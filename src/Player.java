import java.awt.*;
import java.awt.event.KeyEvent;

public class Player
{
    private GameKey upKey;
    private GameKey downKey;
    private int score;

    private Player()
    { /* prevent uninitialized instances */}

    public Player(GameKey key1, GameKey key2)
    {
        switch (key1.getKeyType())
        {
            case UP:
                this.upKey = key1;
                break;
            case DOWN:
                this.downKey = key1;
                break;
        }

        switch (key2.getKeyType())
        {
            case UP:
                this.upKey = key2;
                break;
            case DOWN:
                this.downKey = key2;
                break;
        }

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

    public void resetScore(){
        this.score = 0;
    }

    public int getScore()
    {
        return score;
    }
}
