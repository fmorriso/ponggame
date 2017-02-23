//Name:              Date:

import java.awt.*;

public class Ball extends Polkadot
{
    private double dx;       // pixels to move each time move() is called.
    private double dy;

    // constructors
    public Ball()         //default constructor
    {
        super(200, 200, 50, Color.decode("#008000"));
        dx = getRandomDelta();          // to move vertically
        dy = getRandomDelta();          // to move sideways
    }

    public Ball(double x, double y, double dia, Color c)
    {
        super(x, y, dia, c);
        dx = getRandomDelta();
        dy = getRandomDelta();
    }

    private double getRandomDelta()
    {
        return Math.random() * 14 - 7;
    }

    //modifier methods
    public void setdx(double x)
    {
        dx = x;
    }

    public void setdy(double y)
    {
        dy = y;
    }

    //accessor methods
    public double getdx()
    {
        return dx;
    }

    public double getdy()
    {
        return dy;
    }

    //instance methods
    public void move(double rightEdge, double bottomEdge)
    {
        setX(getX() + dx); // x = x + dx

        setY(getY() + dy); // y = y + dy

        double radius = getRadius();
        double currentX = getX();
        double currentY = getY();

        // check for left & right edge bounces
        double rightEdgeThreshold = rightEdge - radius;
        double leftEdgeThreshold = 0 + radius;
        double bottomEdgeThreshold = bottomEdge - radius;
        double topEdgeThreshold = 0 + radius;

        if (currentX >= rightEdgeThreshold)     // hits the right edge
        {
            setX(rightEdgeThreshold);
            dx = dx * -1;
        } else if (currentX <= leftEdgeThreshold) // hits the left edge
        {
            setX(0 + radius);
            dx = dx * -1;
        } else if (currentY >= bottomEdgeThreshold) // hits the bottom edge
        {
            setY(bottomEdge - radius);
            dy = dy * -1;
        } else if (currentY <= topEdgeThreshold)        // hits top edge
        {
            setY(0 + radius);
            dy = dy * -1;
        }

    }


    public void draw(Graphics myBuffer)
    {
        //ImageIcon pic = new ImageIcon("sleepingbulldog.jpg");
        //myBuffer.drawImage(pic.getImage(),(int)(getX() - getRadius()),(int)(getY() - getRadius()), (int)getDiameter(), (int)getDiameter(), null);
        myBuffer.setColor(getColor());
        myBuffer.fillOval((int) (getX() - getRadius()), (int) (getY() - getRadius()), (int) getDiameter(), (int) getDiameter());
    }

}