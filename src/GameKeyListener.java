import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener
{
    private Thread game;
    private Player leftPlayer;
    private Player rightPlayer;

    private GameKeyListener()
    {/* prevent uninitialized instances */}

    public GameKeyListener(Thread game, Player left, Player right)
    {
        this.game = game;
        this.leftPlayer = left;
        this.rightPlayer = right;
    }

    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        setPlayerKeyPressed(keyCode, true);
    }

    public void keyReleased(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        setPlayerKeyPressed(keyCode, false);
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
        }
    }


    @Override
    public void keyTyped(KeyEvent arg0)
    {


    }
}

