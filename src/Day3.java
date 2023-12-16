import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day3 {
    static char[][] schematic = new char[0][0];
    public static int solveTask1(List<String> input) {
        int sum = 0;
        schematic = readInSchematic(input);
        for (int row = 0; row < schematic.length; row++) {
            for (int col = 0; col < schematic[row].length; col++) {
                if (Character.isDigit(schematic[row][col])) {
                    String number = getNumber(col, row);
                    if (numberNextToSymbol(number, row, col)) {
                        sum+=Integer.parseInt(number);
                    }
                    col+=number.length();
                }
            }
        }
        return sum;
    }

    public static int solveTask2(List<String> input) {
        int sum = 0;
        schematic = readInSchematic(input);
        // We track every symbol with this, the key being the (1D) coordinate of the symbol and value being all the values surrounding that value.
        HashMap<Integer, List<Integer>> componentMap = new HashMap<>();
        for (int row = 0; row < schematic.length; row++) {
            for (int col = 0; col < schematic[row].length; col++) {
                if (Character.isDigit(schematic[row][col])) {
                    String number = getNumber(col, row);
                    if (numberNextToSymbol(number, row, col)) {
                        List<Integer> coordinate1D = coordinatesOfSurroundingSymbols(number, row, col);
                        for (Integer coordinate : coordinate1D) {
                            if (componentMap.containsKey(coordinate)) {
                                componentMap.get(coordinate).add(Integer.parseInt(number));
                            }
                            else {
                                ArrayList<Integer> numberList = new ArrayList<>();
                                numberList.add(Integer.parseInt(number));
                                componentMap.put(coordinate, numberList);
                            }
                        }
                    }
                    col+=number.length();
                }
            }
        }
        for (List<Integer> numbers : componentMap.values()) {
            if (numbers.size()==2) {
                //System.out.println("Multiplying: " + numbers.get(0) + "*" + numbers.get(1));
                sum+= numbers.get(0) * numbers.get(1);
            }
        }
        return sum;
    }

    static boolean numberNextToSymbol(String number, int numberStartRow, int numberStartCol) {
        for (int i = numberStartRow-1; i < numberStartRow+2; i++) {
            if (i<0 || i>=schematic.length) continue;
            for (int j = numberStartCol-1; j < numberStartCol+number.length()+1; j++) {
                if (j<0 || j>=schematic[i].length) continue;
                //System.out.print(schematic[i][j]);
                if (isSymbol(schematic[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    static List<Integer> coordinatesOfSurroundingSymbols(String number, int numberStartRow, int numberStartCol) {
        ArrayList<Integer> coordinates1D = new ArrayList<>();
        for (int i = numberStartRow-1; i < numberStartRow+2; i++) {
            if (i<0 || i>=schematic.length) continue;
            for (int j = numberStartCol-1; j < numberStartCol+number.length()+1; j++) {
                if (j<0 || j>=schematic[i].length) continue;
                if (isSymbol(schematic[i][j])) {
                    coordinates1D.add(i*schematic.length+j);
                }
            }
        }
        return coordinates1D;
    }

    public static boolean isSymbol(char character) {
        return character != '.' && !Character.isDigit(character);
    }

    static String getNumber(int x, int y) {
        if (!Character.isDigit(schematic[y][x])) {
            System.out.println("???");
            return "-1";
        }
        StringBuilder numberString = new StringBuilder();
        int numberEndPoint = x;
        while (numberEndPoint<schematic[y].length && Character.isDigit(schematic[y][numberEndPoint])) {
            numberString.append(schematic[y][numberEndPoint]);
            numberEndPoint++;
        }
        return numberString.toString();
    }

    static char[][] readInSchematic(List<String> input) {
        char[][] schematic = new char[input.size()][input.get(0).length()];

        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).length(); j++) {
                schematic[i][j]=input.get(i).charAt(j);
            }
        }
        return schematic;
    }

    static void printSchematic(char[][] schematic) {
        for (char[] chars : schematic) {
            for (char c : chars) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
}
