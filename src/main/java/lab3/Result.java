package lab3;

import java.io.Serializable;
import java.math.BigDecimal;

public class Result implements Serializable {
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;
    private boolean hit;
    private String time;
    private long nanos;

    public Result() {}

    public Result(BigDecimal x, BigDecimal y, BigDecimal r, boolean hit, String time, long nanos) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.time = time;
        this.nanos = nanos;
    }

    public BigDecimal getX() { return x; }
    public void setX(BigDecimal x) { this.x = x; }

    public BigDecimal getY() { return y; }
    public void setY(BigDecimal y) { this.y = y; }

    public BigDecimal getR() { return r; }
    public void setR(BigDecimal r) { this.r = r; }

    public boolean isHit() { return hit; }
    public void setHit(boolean hit) { this.hit = hit; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public long getNanos() { return nanos; }
    public void setNanos(long nanos) { this.nanos = nanos; }
}