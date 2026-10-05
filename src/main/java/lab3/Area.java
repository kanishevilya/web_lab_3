package lab3;

public final class Area {
    private Area(){}

    public static boolean contains(double x, double y, double r) {
        if (x <= 0 && y >= 0) {
            return x >= -r / 2 && y <= 2 * x + r;
        }
        if (x >= 0 && y >= 0) {
            return x * x + y * y <= r * r;
        }
        if (x >= 0 && y <= 0) {
            return x <= r && y >= -r / 2;
        }
        return false;
    }

}
