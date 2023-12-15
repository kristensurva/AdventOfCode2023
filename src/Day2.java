import java.util.Arrays;
import java.util.List;

public class Day2 {
    public static int solveTask1(List<String> input) {
        int redCubesInBag = 12;
        int greenCubesInBag = 13;
        int blueCubesInBag = 14;
        int sum = 0;
        for (String line : input) {
            String[] split = line.split(":|;");
            int gameID = Integer.parseInt(split[0].substring(4).trim());
            boolean isGameEligible = true;
            for (String patch : Arrays.copyOfRange(split, 1, split.length)) {
                if (!isGameEligible) {
                    break;
                }
                for (String string : patch.split(",")) {
                    //System.out.println(string);
                    String[] patchSplit = string.trim().split(" ");
                    int amount = Integer.parseInt(patchSplit[0]);
                    switch (patchSplit[1]) {
                        case "red":
                            if (amount > redCubesInBag) {
                                isGameEligible = false;
                            }
                        case "green":
                            if (amount > greenCubesInBag) {
                                isGameEligible = false;
                            }
                        case "blue":
                            if (amount > blueCubesInBag) {
                                isGameEligible = false;
                            }
                    }
                    if (!isGameEligible) {
                        break;
                    }
                }
            }
            if (isGameEligible) {
                sum += gameID;
            }
        }
        return sum;
    }

    public static int solveTask2(List<String> input) {
        int sum = 0;
        for (String line : input) {
            int redCubesMaxCurrentGame = 0;
            int greenCubesMaxCurrentGame = 0;
            int blueCubesMaxCurrentGame = 0;
            String[] split = line.split(":|;");
            for (String patch : Arrays.copyOfRange(split, 1, split.length)) {
                for (String string : patch.split(",")) {
                    String[] patchSplit = string.trim().split(" ");
                    int amount = Integer.parseInt(patchSplit[0]);
                    switch (patchSplit[1]) {
                        case "red" -> redCubesMaxCurrentGame = Integer.max(redCubesMaxCurrentGame, amount);
                        case "green" -> greenCubesMaxCurrentGame = Integer.max(greenCubesMaxCurrentGame, amount);
                        case "blue" -> blueCubesMaxCurrentGame = Integer.max(blueCubesMaxCurrentGame, amount);
                    }
                }
            }
            int powerOfSet = redCubesMaxCurrentGame*greenCubesMaxCurrentGame*blueCubesMaxCurrentGame;
            sum+=powerOfSet;
        }
        return sum;
    }
}
