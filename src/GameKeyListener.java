import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener
{
    private Thread game;
    private Player leftPlayer;
    private Player rightPlayer;
    private GameKey stopGameKey;
    private GameKey resetGameKey;

    private GameKeyListener()
    {/* prevent uninitialized instances */}

    public GameKeyListener(Thread game, Player left, Player right, GameKey stopGameKey, GameKey resetGameKey)
    {
        this.game = game;
        this.leftPlayer = left;
        this.rightPlayer = right;
        this.stopGameKey = stopGameKey;
        this.resetGameKey = resetGameKey;
    }

    public void keyPressed(KeyEvent e)
    {
        setPlayerKeyPressed(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e)
    {
        setPlayerKeyPressed(e.getKeyCode(), false);
    }

    private void setPlayerKeyPressed(int keyCode, boolean pressed)
    {
        if (keyCode == leftPlayer.getKeyCode(GameKeyType.UP))
        {
            leftPlayer.setKeyPressed(GameKeyType.UP, pressed);
        } else if (keyCode == leftPlayer.getKeyCode(GameKeyType.DOWN))
        {
            leftPlayer.setKeyPressed(GameKeyType.DOWN, pressed);
        } else if (keyCode == rightPlayer.getKeyCode(GameKeyType.UP))
        {
            rightPlayer.setKeyPressed(GameKeyType.UP, pressed);
        } else if (keyCode == rightPlayer.getKeyCode(GameKeyType.DOWN))
        {
            rightPlayer.setKeyPressed(GameKeyType.DOWN, pressed);
        } else if(keyCode == stopGameKey.getKeyCode()) {
            stopGameKey.setPressed(true);
        } else if(keyCode == resetGameKey.getKeyCode()){
            resetGameKey.setPressed(true);
        }
    }


    @Override
    public void keyTyped(KeyEvent arg0)
    {


    }
}

