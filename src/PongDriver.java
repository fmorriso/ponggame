// Fred Morrison - March 2017

import javax.swing.JFrame;
import java.awt.*;

public class PongDriver
{
    public static void main(String[] args)
    {
        String version = System.getProperty("java.version");
        JFrame frame = new JFrame(String.format("Pong - java version %s", version));

        Dimension scaledSize = getScaledSize(0.75, 100);
        // keep window square
        frame.setSize(scaledSize.height, scaledSize.height);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PongGame p = new PongGame();

        frame.setContentPane(p);
        p.requestFocus();
        frame.setVisible(true);
    }

    /**
     * Gets a rectangle that is scaled to a percentage of available device screen size,
     * rounded to the specified multiple.
     *
     * @param pct - the percentage (> 0 and < 1.0) of available device screen size to use.
     * @param multipleOf - value to round up the scaled size to be a multiple of.
     * @return - a Dimension object that holds the scaled width and height.
     */
    private static Dimension getScaledSize(double pct, int multipleOf)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(screenSize);
        // sanity check for unreasonable percentage values
        if (pct < 0.1 || pct > 1)
            return screenSize;
        System.out.format("Screen width=%d, height=%d%n", screenSize.width, screenSize.height);
        final int frameHeight = (int) (screenSize.height * pct) / multipleOf * multipleOf;
        final int frameWidth = (int) (screenSize.width * pct) / multipleOf * multipleOf;
        Dimension frameSize = new Dimension(frameWidth, frameHeight);
        System.out.format("scaled frame: width=%d, height=%d%n", frameWidth, frameHeight);
        return frameSize;
    }
}
