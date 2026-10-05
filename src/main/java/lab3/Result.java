package lab3;

import java.io.Serializable;

public class Result implements Serializable {
    private final double x;
    private final double y;
    private final double r;
    private final boolean hit;
    private final String time;
    private final long nanos;

    public Result(double x, double y, double r, boolean hit, String time, long nanos) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.time = time;
        this.nanos = nanos;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
    public boolean isHit() { return hit; }
    public String getTime() { return time; }
    public long getNanos() { return nanos; }

}
