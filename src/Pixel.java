import java.awt.Color;

public class Pixel extends Color {
    int x = 0, y = 0;
    static int counter = 0;

    public Pixel(int red, int green, int blue, int x, int y) {
        super(red, green, blue, 0);
        this.x = x;
        this.y = y;
        counter++;
    }
}