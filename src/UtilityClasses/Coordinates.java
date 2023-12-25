package UtilityClasses;

public class Coordinates {
    public int row;
    public int col;

    public Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinates) {
            return this.row == ((Coordinates) obj).row && this.col == ((Coordinates) obj).col;
        }
        return false;
    }

    public static Coordinates sum(Coordinates point1, Coordinates point2) {
        return new Coordinates(point1.row+point2.row, point1.col+point2.col);
    }

    @Override
    public String toString() {
        return "[" + row + ", " + col + "]";
    }
}
