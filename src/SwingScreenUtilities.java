import java.awt.*;

public class SwingScreenUtilities {
    /**
     * Gets a rectangle that is scaled to a percentage of available device screen
     * size,
     * rounded to the specified multiple.
     *
     * @param pct        - the percentage (> 0 and < 1.0) of available device screen
     *                   size to use.
     * @param multipleOf - value to round up the scaled size to be a multiple of.
     * @return - a Dimension object that holds the scaled width and height.
     */
    public static Dimension getScaledSize(double pct, int multipleOf) {
        return getScaledSize(pct, multipleOf, false);
    }

    /**
     * Gets a rectangle that is scaled to a percentage of available device screen
     * size, rounded to the specified multiple and optionally square sized.
     *
     * @param pct        - the percentage (> 0 and < 1.0) of available device screen
     *                   size to use.
     * @param multipleOf - value to round up the scaled size to be a multiple of.
     * @param wantSquare - true if the scaled size should have equal width and height,
     *                   using the smaller of the device width and height.
     * @return - a Dimension object that holds the scaled width and height.
     */
    public static Dimension getScaledSize(double pct, int multipleOf, boolean wantSquare) {
        Dimension screenSize = getDefaultScreenSize();
        // sanity check for unreasonable percentage values
        if (pct < 0.1 || pct > 1)
            return screenSize;

        Dimension frameSize = getDimension(pct, multipleOf);
        if (wantSquare) {
            // make the frame size square
            int n = (int) (Math.min(frameSize.getWidth(), frameSize.getHeight()));
            frameSize.setSize(n, n);
        }

        return frameSize;
    }

    /**
     * Gets a rectangle that is scaled to a percentage of available device screen
     * size, rounded to the specified multiple and optionally square sized.
     *
     * @param pct        - the percentage (> 0 and < 1.0) of available device screen
     *                   size to use.
     * @param multipleOf - value to round up the scaled size to be a multiple of.
     * @return - a Dimension object that holds the scaled width and height.
     */
    private static Dimension getDimension(double pct, int multipleOf) {
        Dimension screenSize = getDefaultScreenSize();
        // sanity check for unreasonable percentage values
        if (pct < 0.1 || pct > 1)
            return screenSize;

        // System.out.format("Screen width=%d, height=%d%n", screenSize.width,
        // screenSize.height);
        final int frameHeight = (int) (screenSize.height * pct) / multipleOf * multipleOf;
        final int frameWidth = (int) (screenSize.width * pct) / multipleOf * multipleOf;
        Dimension frameSize = new Dimension(frameWidth, frameHeight);
        // System.out.format("scaled frame: width=%d, height=%d%n", frameWidth,
        // frameHeight);
        return frameSize;
    }

    /**
     * Gets the default screen size of the device this program is running on.
     *
     * @return Dimension containing the default screen width and height.
     */
    private static Dimension getDefaultScreenSize() {
        GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = genv.getDefaultScreenDevice();
        DisplayMode mode = device.getDisplayMode();

        return new Dimension(mode.getWidth(), mode.getHeight());
    }

}
