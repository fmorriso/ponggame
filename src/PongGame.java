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

    private double getRandomDelta(){
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

            else if (StopGameKey.isPressed()){
                this.gameIsActive = false;
            } else if(ResetGameKey.isPressed()){
                ResetGame();
            }

            // clear buffer and move ball
            myBuffer.setColor(BACKGROUND);
            myBuffer.fillRect(0, 0, FRAME, FRAME);
            ball.move(FRAME, FRAME);

            // check for collisions
            if (BumperCollision.isCollision(bumperLeft, ball))
            {
                hits++;
                BumperCollision.collide(bumperLeft, ball);
            }

            if (BumperCollision.isCollision(bumperRight, ball))
            {
                hits++;
                BumperCollision.collide(bumperRight, ball);
            }

            if ( ( ball.getX() - ball.getRadius() ) <= 0)
            {
                rightPlayer.rewardPlayer();

                ball.setX(FRAME / 2);
                ball.setY(FRAME / 2);
                ball.setdx(getRandomDelta());
                ball.setdy(getRandomDelta());
                //game.sleep(1000);
                //record = (record>hits)? record : hits;
                //hits = 0;
            }

            if ( (ball.getX() + ball.getRadius() ) >= FRAME)
            {
                leftPlayer.rewardPlayer();

                ball.setX(FRAME / 2);
                ball.setY(FRAME / 2);
                ball.setdx(getRandomDelta());
                ball.setdy(getRandomDelta());

                drawScreen();


                //repaint();
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {

                    e.printStackTrace();
                }

                //record = (record>hits)? record : hits;
                //hits =0;
            }

            drawScreen();

            try
            {
                Thread.sleep(50);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void ResetGame() {
        leftPlayer.resetScore();
        rightPlayer.resetScore();
    }

    private void drawScreen()
    {
        ball.draw(myBuffer);
        bumperRight.draw(myBuffer);
        bumperLeft.draw(myBuffer);

        // customize the bumpers and ball
        //myBuffer.setColor(BACKGROUND);
        //myBuffer.fillOval((int) (ball.getX() - ball.getRadius() / 2), (int) (ball.getY() - ball.getRadius() / 2), (int) (ball.getRadius()), (int) (ball.getRadius()));
        //myBuffer.fillRect(bumperLeft.getX(), bumperLeft.getY() + bumperLeft.getXWidth() / 4, bumperLeft.getXWidth() * 3 / 4, bumperLeft.getYWidth() - bumperLeft.getXWidth() / 2);
        //myBuffer.fillRect(bumperRight.getX() + bumperRight.getXWidth() / 4, bumperRight.getY() + bumperRight.getXWidth() / 4, bumperRight.getXWidth() * 3 / 4, bumperRight.getYWidth() - bumperRight.getXWidth() / 2);


        // update hits on buffer
        myBuffer.setColor(Color.white);
        myBuffer.setFont(new Font("Monospaced", Font.BOLD, FRAME / 20));

        updateScoreBoard();

        repaint();

    }

    private void updateScoreBoard(){

        String message = "Player Left:" + leftPlayer.getScore();
        int x = 0;
        myBuffer.drawString(message, x, getFrameSize() / 16);
        if (leftPlayer.getScore() == WinningScore) {
            this.gameIsActive = false;
            message = "WINNER";
            myBuffer.drawString(message, x, getFrameSize() / 8);
        }

        message = "Player Right:" + rightPlayer.getScore();
        x = getFrameSize() / 2;
        myBuffer.drawString(message, x, getFrameSize() / 16);
        if (rightPlayer.getScore() == WinningScore) {
            this.gameIsActive = false;
            message = "WINNER";
            myBuffer.drawString(message, x, getFrameSize() / 8);
        }

        if (this.gameIsActive == false){
            message = "GAME STOP KEY WAS PRESSED";
            myBuffer.drawString(message, getFrameSize() / 6, getFrameSize() / 3);
        }
    }

}
