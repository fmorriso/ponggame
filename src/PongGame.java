import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.KeyEvent;

public class PongGame extends JPanel implements Runnable
{
    private static final long serialVersionUID = 1L;
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
    private boolean pointWasScored;

    private boolean gameIsActive;
    private static final GameKey StopGameKey = new GameKey(GameKeyType.STOP, KeyEvent.VK_CLOSE_BRACKET);
    private static final GameKey ResetGameKey = new GameKey(GameKeyType.RESET, KeyEvent.VK_R);

    private static final int WinningScore = 5;

    private static final double minimumDelta = 10;

    private static final int ballMovementPausInterval = 50;
    private static final int gameResetPauseInterval = 2000;
    private static final int pointScoredPauseInterval = 1500;

    private static final Font scoreboardFront = new Font("Monospaced", Font.BOLD, FRAME / 22);

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
        double computedDelta = Math.random() * FRAME / 40;
        computedDelta = Math.max(computedDelta, minimumDelta);
        return computedDelta;
    }

    private int getFrameSize()
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
                bumperLeft.setY((bumperLeft.getYbottom() >= FRAME) ? bumperLeft.getY() : bumperLeft.getY() + INCREM);

            else if (rightPlayer.keyPressed(GameKeyType.UP))
                bumperRight.setY((bumperRight.getY() <= 0) ? bumperRight.getY() : bumperRight.getY() - INCREM);

            else if (rightPlayer.keyPressed(GameKeyType.DOWN))
                bumperRight.setY((bumperRight.getY() + bumperRight.getYWidth() >= FRAME) ? bumperRight.getY() : bumperRight.getY() + INCREM);

            else if (StopGameKey.isPressed())
            {
                this.gameIsActive = false;
            } else if (ResetGameKey.isPressed())
            {
                resetGame();
            }

            // clear buffer and move ball
            myBuffer.setColor(BACKGROUND);
            myBuffer.fillRect(0, 0, getFrameSize(), getFrameSize());
            ball.move(getFrameSize(), getFrameSize());

            pointWasScored = false;

            // Check to see if ball hit a bumper or was missed by one of the players
            if (BumperCollision.isCollision(bumperLeft, ball))
            {
                // ball hit player on the left bumper, so bound off it
                BumperCollision.collide(bumperLeft, ball);
            } else if (BumperCollision.isCollision(bumperRight, ball))
            {
                // ball hit player on the right bumper, so bound off of it
                BumperCollision.collide(bumperRight, ball);
            } else if ((ball.getX() - ball.getRadius()) <= 0)
            {
                // player on left missed the ball, reward the player on the right
                rightPlayer.rewardPlayer();
                pointWasScored = true;
                resetBallToStartingPosition();
            }
            else if ((ball.getX() + ball.getRadius()) >= getFrameSize() )
            {
                // player on right missed the ball, reward the player on the left
                leftPlayer.rewardPlayer();
                pointWasScored = true;
                resetBallToStartingPosition();
            }

            drawScreen();
        }
    }

    private void resetBallToStartingPosition()
    {
        // return ball to the middle of the screen
        int middle = getFrameSize() / 2;
        ball.setX(middle);
        ball.setY(middle);

        // change speed and direction of the ball
        ball.setdx(getRandomDelta() * coinFlip() );
        ball.setdy(getRandomDelta() * coinFlip() );
    }

    private int coinFlip()
    {
        double r = Math.random() * 2;
        int i = (int) (r);
        if (i == 0) return -1;
        return 1;
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

        updateScoreBoard();

        repaint();

        // pause to allow players to see ball movement, a point was scored, or game was reset message
        int pauseInterval = ballMovementPausInterval;
        if (ResetGameKey.isPressed())
        {
            pauseInterval = gameResetPauseInterval;
            ResetGameKey.setPressed(false);
        } else if (pointWasScored)
        {
            pauseInterval = pointScoredPauseInterval;
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
        myBuffer.setFont(scoreboardFront);

        String message = "Player Left:" + leftPlayer.getScore();
        int x = 0;
        int yScore = getFrameSize() / 16;
        int yWinner = getFrameSize() / 8;

        myBuffer.drawString(message, x, yScore);
        if (leftPlayer.getScore() == WinningScore)
        {
            this.gameIsActive = false;
            message = "WINNER";
            myBuffer.drawString(message, x, yWinner);
        }

        message = "Player Right:" + rightPlayer.getScore();
        x = getFrameSize() / 2;
        myBuffer.drawString(message, x, yScore);
        if (rightPlayer.getScore() == WinningScore)
        {
            this.gameIsActive = false;
            message = "WINNER";
            myBuffer.drawString(message, x, yWinner);
        }

        // Display message in (approximately) middle of screen if any of the special game control keys were pressed:
        x = getFrameSize() / 6;
        int yMiddle = getFrameSize() / 3;
        if (StopGameKey.isPressed())
        {
            message = "GAME STOP KEY WAS PRESSED";
            myBuffer.drawString(message, x, yMiddle);
        } else if (ResetGameKey.isPressed())
        {
            message = "GAME RESET KEY WAS PRESSED";
            myBuffer.drawString(message, x, yMiddle);
        }
    }
}
