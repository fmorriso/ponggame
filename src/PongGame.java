// Fred Morrison - March 2017

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.KeyEvent;

public class PongGame extends JPanel implements Runnable
{
    private static final int FRAME = 800;
    private final int INCREM = FRAME / 20;
    private static final Color BACKGROUND = Color.decode("#240062"); //.black;
    private static final Color BALL_COLOR = Color.green;
    private static final Color BUMPER_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.WHITE;

    private static final double BALL_DIAM = 50;

    private Player leftPlayer, rightPlayer;

    private Thread game;

    private BufferedImage myImage;
    private Graphics myBuffer;

    private Ball ball;
    private Bumper bumperLeft, bumperRight;

    private int hits, record;

    private boolean gameIsActive;
    private static final GameKey StopGameKey = new GameKey(GameKeyType.STOP, KeyEvent.VK_CLOSE_BRACKET);
    private static final GameKey ResetGameKey = new GameKey(GameKeyType.RESET, KeyEvent.VK_R);

    private static final int WinningScore = 5;
    private final int ballMovementPauseInterval = 50;
    private final int gameResetPauseInterval = 2000;

    public PongGame()
    {
        myImage = new BufferedImage(FRAME, FRAME, BufferedImage.TYPE_INT_RGB);
        myBuffer = myImage.getGraphics();

        // create ball and jump
        ball = new Ball(FRAME / 2, FRAME / 2, BALL_DIAM, BALL_COLOR);
        ball.setdx(getRandomDelta());
        ball.setdy(getRandomDelta());

        // create bumpers
        bumperLeft = new Bumper(5, FRAME / 2, INCREM, FRAME / 4, BUMPER_COLOR);
        bumperRight = new Bumper(FRAME - INCREM, FRAME / 2, INCREM, FRAME / 4, BUMPER_COLOR);

        record = 0;
        hits = 0;

        // create players and select their choice of up/down keys
        leftPlayer = new Player(new GameKey(GameKeyType.UP, KeyEvent.VK_A),
                new GameKey(GameKeyType.DOWN, KeyEvent.VK_Z));

        rightPlayer = new Player(new GameKey(GameKeyType.UP, KeyEvent.VK_UP),
                new GameKey(GameKeyType.DOWN, KeyEvent.VK_DOWN));

        this.gameIsActive = true;

        game = new Thread(this);

        GameKeyListener listener = new GameKeyListener(game, leftPlayer, rightPlayer, StopGameKey, ResetGameKey);
        addKeyListener(listener);

        setFocusable(true);
        game.start();
    }

    private double getRandomDelta()
    {
        double minimumDelta = 10;
        double computedDelta = Math.random() * FRAME / 40;
        computedDelta = Math.max(computedDelta, minimumDelta);
        return computedDelta;
    }

    public int getFrameSize()
    {
        return FRAME;
    }

    public void paintComponent(Graphics g)
    {
        g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
    }

    public void run()
    {
        while (this.gameIsActive)
        {
            if (leftPlayer.keyPressed(GameKeyType.UP))
                bumperLeft.setY((bumperLeft.getY() <= 0) ? bumperLeft.getY() : bumperLeft.getY() - INCREM);

            else if (leftPlayer.keyPressed(GameKeyType.DOWN))
                bumperLeft.setY((bumperLeft.getY() + bumperLeft.getYWidth() >= FRAME) ? bumperLeft.getY() : bumperLeft.getY() + INCREM);

            else if (rightPlayer.keyPressed(GameKeyType.UP))
                bumperRight.setY((bumperRight.getY() <= 0) ? bumperRight.getY() : bumperRight.getY() - INCREM);

            else if (rightPlayer.keyPressed(GameKeyType.DOWN))
                bumperRight.setY((bumperRight.getY() + bumperRight.getYWidth() >= FRAME) ? bumperRight.getY() : bumperRight.getY() + INCREM);

            else if (StopGameKey.isPressed())
                this.gameIsActive = false;

            else if (ResetGameKey.isPressed())
                resetGame();

            // clear buffer and move ball
            myBuffer.setColor(BACKGROUND);
            myBuffer.fillRect(0, 0, FRAME, FRAME);
            ball.move(FRAME, FRAME);

            // check for collisions
            if (BumperCollision.isCollision(bumperLeft, ball))
            {
                hits++;
                BumperCollision.collide(bumperLeft, ball);
            } else if (BumperCollision.isCollision(bumperRight, ball))
            {
                hits++;
                BumperCollision.collide(bumperRight, ball);
            }

            // did the player on the left miss the ball coming at his bumper ?
            if ((ball.getX() - ball.getRadius()) <= 0)
            {
                // Yes, reward the player on the right
                rightPlayer.rewardPlayer();
                resetBallToStartingPosition();
            }
            // did the player on the right miss the ball coming at his bumper?
            else if ((ball.getX() + ball.getRadius()) >= FRAME)
            {
                // Yes, reward the player on the left
                leftPlayer.rewardPlayer();
                resetBallToStartingPosition();
            }

            drawScreen();
        }
    }

    private void resetBallToStartingPosition()
    {
        ball.setX(FRAME / 2);
        ball.setY(FRAME / 2);
        ball.setdx(getRandomDelta());
        ball.setdy(getRandomDelta());
    }

    private void resetGame()
    {
        leftPlayer.resetScore();
        rightPlayer.resetScore();
        resetBallToStartingPosition();
    }

    private void drawScreen()
    {
        ball.draw(myBuffer);
        bumperRight.draw(myBuffer);
        bumperLeft.draw(myBuffer);

        // update hits on buffer

        updateScoreBoard();

        repaint();

        // pause to allow players to see ball movement or game reset message
        int pauseInterval = ballMovementPauseInterval;
        if (ResetGameKey.isPressed())
        {
            pauseInterval = gameResetPauseInterval;
            ResetGameKey.setPressed(false);
        }
        try
        {
            Thread.sleep(pauseInterval);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void updateScoreBoard()
    {

        myBuffer.setColor(TEXT_COLOR);
        myBuffer.setFont(new Font("Monospaced", Font.BOLD, FRAME / 22));

        String message = "Player Left:" + leftPlayer.getScore();
        int x = 0;
        myBuffer.drawString(message, x, getFrameSize() / 16);
        if (leftPlayer.getScore() == WinningScore)
        {
            this.gameIsActive = false;
            message = "WINNER";
            myBuffer.drawString(message, x, getFrameSize() / 8);
        }

        message = "Player Right:" + rightPlayer.getScore();
        x = getFrameSize() / 2;
        myBuffer.drawString(message, x, getFrameSize() / 16);
        if (rightPlayer.getScore() == WinningScore)
        {
            this.gameIsActive = false;
            message = "WINNER";
            myBuffer.drawString(message, x, getFrameSize() / 8);
        }

        // Display message in (approximately) middle of screen if any of the special game control keys were pressed:
        x = getFrameSize() / 6;
        if (StopGameKey.isPressed())
        {
            message = "GAME STOP KEY WAS PRESSED";
            myBuffer.drawString(message, x, getFrameSize() / 3);
        } else if (ResetGameKey.isPressed())
        {
            message = "GAME RESET KEY WAS PRESSED";
            myBuffer.drawString(message, x, getFrameSize() / 3);
        }
    }
}
