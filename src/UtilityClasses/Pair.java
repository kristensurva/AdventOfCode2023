package UtilityClasses;

public class Pair {
    public Object p1;
    public Object p2;
    public long distance;

    public Pair(Object p1, Object p2, long distance) {
        this.p1 = p1;
        this.p2 = p2;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "{"+ p1 + ", " + p2 + "}" + ", distance=" + distance;
    }
}
