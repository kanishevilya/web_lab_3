package lab3;

import java.math.BigDecimal;

public final class Area {
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal TWO = new BigDecimal("2");

    private Area() {}

    public static boolean contains(BigDecimal x, BigDecimal y, BigDecimal r) {
        if (x.compareTo(ZERO) <= 0 && y.compareTo(ZERO) >= 0) {
            BigDecimal twoX = x.multiply(TWO);
            boolean hypotenuse = y.subtract(twoX).compareTo(r) <= 0;
            boolean leftBound = twoX.compareTo(r.negate()) >= 0;
            return hypotenuse && leftBound;
        }

        if (x.compareTo(ZERO) >= 0 && y.compareTo(ZERO) >= 0) {
            BigDecimal x2 = x.multiply(x);
            BigDecimal y2 = y.multiply(y);
            BigDecimal r2 = r.multiply(r);
            return x2.add(y2).compareTo(r2) <= 0;
        }

        if (x.compareTo(ZERO) >= 0 && y.compareTo(ZERO) <= 0) {
            BigDecimal twoY = y.multiply(TWO);
            return x.compareTo(r) <= 0 && twoY.compareTo(r.negate()) >= 0;
        }

        return false;
    }
}