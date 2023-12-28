import UtilityClasses.Pair;
import UtilityClasses.Point;
import UtilityClasses.PointLong;

import java.util.*;
import java.util.List;

public class Day11 {
    static List<List<Character>> image;

    static long solveTask1(List<String> input) {
        image = processInput(input);
        // Add columns
        for (int col = 0; col < image.get(0).size(); col++) {
            if (isColumnEmpty(col)) {
                addColumnAtIndex(col);
                col++;
            }
        }
        // Add rows
        for (int row = 0; row < image.size(); row++) {
            if (isRowEmpty(row)) {
                addRowAtIndex(row);
                row++;
            }
        }
        printImage();
        // Measure distances
        ArrayList<Point> galaxyPositions = new ArrayList<>();
        ArrayList<Pair> galaxyPairs = new ArrayList<>();
        for (int row = 0; row < image.size(); row++) {
            for (int col = 0; col < image.get(0).size(); col++) {
                if (image.get(row).get(col)=='#') {
                    galaxyPositions.add(new Point(row, col));
                }
            }
        }
        // Making the pairs
        for (int i = 0; i < galaxyPositions.size(); i++) {
            for (int j = i+1; j < galaxyPositions.size(); j++) {
                Point galaxy1 = galaxyPositions.get(i);
                Point galaxy2 = galaxyPositions.get(j);
                int distance = Point.manhattanDistance(galaxy1, galaxy2);
                galaxyPairs.add(new Pair(galaxy1, galaxy2, distance));
            }
        }
        //System.out.println(galaxyPairs);
        return galaxyPairs.stream().reduce(0L, (acc, e) -> acc + e.distance, Long::sum);
    }

    static long solveTask2(List<String> input) {
        final int emptySpaceMultiplier = 1000000-1;
        image = processInput(input);
        ArrayList<Integer> emptyRows = new ArrayList<>();
        ArrayList<Integer> emptyColumns = new ArrayList<>();
        // Add columns
        for (int col = 0; col < image.get(0).size(); col++) {
            //System.out.println("Col: " + col);
            if (isColumnEmpty(col)) {
                emptyColumns.add(col);
            }
        }
        // Add rows
        for (int row = 0; row < image.size(); row++) {
            //System.out.println("Row: " + row);
            if (isRowEmpty(row)) {
                emptyRows.add(row);
            }
        }
        //printImage();
        // Measure distances
        ArrayList<PointLong> galaxyPositions = new ArrayList<>();
        ArrayList<Pair> galaxyPairs = new ArrayList<>();
        for (int row = 0; row < image.size(); row++) {
            for (int col = 0; col < image.get(0).size(); col++) {
                if (image.get(row).get(col)=='#') {
                    int finalRow = row;
                    int rowMultiplier = emptyRows.stream().filter(e -> e < finalRow).reduce(0 , (acc, e) -> acc+1);
                    int finalCol = col;
                    int colMultiplier = emptyColumns.stream().filter(e -> e < finalCol).reduce(0 , (acc, e) -> acc+1);
                    galaxyPositions.add(new PointLong(row + (long) rowMultiplier * emptySpaceMultiplier, col + (long) colMultiplier * emptySpaceMultiplier));
                }
            }
        }
        // Making the pairs
        for (int i = 0; i < galaxyPositions.size(); i++) {
            for (int j = i+1; j < galaxyPositions.size(); j++) {
                PointLong galaxy1 = galaxyPositions.get(i);
                PointLong galaxy2 = galaxyPositions.get(j);
                long distance = PointLong.manhattanDistance(galaxy1, galaxy2);
                galaxyPairs.add(new Pair(galaxy1, galaxy2, distance));
            }
        }
        //System.out.println(galaxyPairs);
        return galaxyPairs.stream().reduce(0L, (acc, e) -> acc + e.distance, Long::sum);
    }

    static List<List<Character>> processInput(List<String> input) {
        List<List<Character>> image = new ArrayList<>(input.size());
        input.forEach(e -> image.add(new ArrayList<>()));
        //char[][] image = new char[input.size()][input.get(0).length()];
        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(0).length(); col++) {
                image.get(row).add(input.get(row).charAt(col));
            }
        }
        return image;
    }

    static boolean isColumnEmpty(int col) {
        for (int row = 0; row < image.size(); row++) {
            if (image.get(row).get(col)!='.') {
                return false;
            }
        }
        return true;
    }

    static boolean isRowEmpty(int row) {
        for (int col = 0; col < image.get(0).size(); col++) {
            if (image.get(row).get(col)!='.') {
                return false;
            }
        }
        return true;
    }
    
    static void addColumnAtIndex(int col) {
        for (int row = 0; row < image.size(); row++) {
            image.get(row).add(col, '.');
        }
    }

    static void addRowAtIndex(int row) {
        image.add(row, new ArrayList<>());
        for (int col = 0; col < image.get(0).size(); col++) {
            image.get(row).add('.');
        }
    }

    static void printImage() {
        for (int row = 0; row < image.size(); row++) {
            for (int col = 0; col < image.get(0).size(); col++) {
                System.out.print(image.get(row).get(col));
            }
            System.out.println();
        }
        System.out.println();
    }
}
