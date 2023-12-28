package UtilityClasses;

public class Point {
    // On a traditional coordinate plane, x = col, y = row
    public int row;
    public int col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            return this.row == ((Point) obj).row && this.col == ((Point) obj).col;
        }
        return false;
    }

    public static Point sum(Point point1, Point point2) {
        return new Point(point1.row+point2.row, point1.col+point2.col);
    }

    public static int manhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.row - p2.row) + Math.abs(p1.col - p2.col);
    }

    @Override
    public String toString() {
        return "[" + row + ", " + col + "]";
    }
}
