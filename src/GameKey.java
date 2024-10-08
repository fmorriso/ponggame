// Fred Morrison - March 2017
public class GameKey
{
    private GameKeyType keyType;
    private int keyCode;
    private boolean pressed;

    private GameKey() {/* prevent uninitialized instances */}

    public GameKey(GameKeyType type, int keyCode) {
        this.keyType = type;
        this.keyCode = keyCode;
        this.pressed = false;
    }

    public GameKeyType getKeyType() {
        return keyType;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

}
