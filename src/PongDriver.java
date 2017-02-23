// Torbert, 7.20.06

import javax.swing.JFrame;
public class PongDriver
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Unit 2: Pong");
        frame.setSize(400, 400);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PongGame p = new PongGame();

        frame.setContentPane(p);
        p.requestFocus();
        frame.setVisible(true);

    }
}
