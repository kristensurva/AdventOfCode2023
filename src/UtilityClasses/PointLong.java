package UtilityClasses;

public class PointLong {
    // On a traditional coordinate plane, x = col, y = row
    public long row;
    public long col;

    public PointLong(long row, long col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UtilityClasses.Point) {
            return this.row == ((UtilityClasses.Point) obj).row && this.col == ((UtilityClasses.Point) obj).col;
        }
        return false;
    }

    public static PointLong sum(PointLong point1, PointLong point2) {
        return new PointLong(point1.row+point2.row, point1.col+point2.col);
    }

    public static long manhattanDistance(PointLong p1, PointLong p2) {
        return Math.abs(p1.row - p2.row) + Math.abs(p1.col - p2.col);
    }

    @Override
    public String toString() {
        return "[" + row + ", " + col + "]";
    }
}
