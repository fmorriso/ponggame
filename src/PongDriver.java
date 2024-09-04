// Fred Morrison - March 2017

import javax.swing.JFrame;
import java.awt.*;

public class PongDriver
{
    public static void main(String[] args)
    {
    	String version = String.format("Java version: %s%n", getJavaVersion());
        System.out.println(version);
        JFrame frame = new JFrame(String.format("Pong - java version %s", version));
        
        // scale window to a pct of device size, keeping the result size square
        Dimension scaledSize = SwingScreenUtilities.getScaledSize(0.55, 100, true);
        
        frame.setSize(scaledSize);
        // center the window on the screen
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PongGame p = new PongGame();

        frame.setContentPane(p);
        p.requestFocus();
        frame.setVisible(true);
    }

    
    /** get the java version that is running the current program
     * @return string containing the java version running the current program
     */
    private static String getJavaVersion()
    {
        Runtime.Version runTimeVersion = Runtime.version();
        return String.format("%s.%s.%s.%s", runTimeVersion.feature(), runTimeVersion.interim(), runTimeVersion.update(), runTimeVersion.patch());
    }
}
