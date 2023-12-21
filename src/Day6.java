import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 {
    public static int solveTask1(List<String> input) {
        String[] temp = input.get(0).split(" +");
        Integer[] raceDurations  = Arrays.stream(Arrays.copyOfRange(temp, 1, temp.length)).map(((e -> Integer.parseInt(e)))).toArray(Integer[]::new);
        temp = input.get(1).split(" +");
        Integer[] raceRecords  = Arrays.stream(Arrays.copyOfRange(temp, 1, temp.length)).map(((e -> Integer.parseInt(e)))).toArray(Integer[]::new);
        int[] winningPossibilities = new int[raceRecords.length];
        for (int i = 0; i < raceDurations.length; i++) {
            for (int j = 1; j < raceDurations[i]; j++) {
                int distance = j*(raceDurations[i]-j);
                if (distance>raceRecords[i]) {
                    winningPossibilities[i]++;
                }
            }
        }
        return Arrays.stream(winningPossibilities).reduce((acc, e) -> acc*e).getAsInt();
    }

    public static long solveTask2(List<String> input) {
        long raceDuration = Long.parseLong(input.get(0).replace(" ", "").split(":")[1]);
        long raceRecord = Long.parseLong(input.get(1).replace(" ", "").split(":")[1]);
        long winningPossibilities = 0;
        for (long i = 1; i < raceDuration; i++) {
            long distance = i*(raceDuration-i);
            if (distance>raceRecord) {
                winningPossibilities++;
            }
        }
        return winningPossibilities;
    }
}

