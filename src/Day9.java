import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {
    static int solveTask1(List<String> input) {
        int finalSum = 0;
        for (String line : input) {
            List<Integer> numbers =  Arrays.stream(line.split(" ")).map(e -> Integer.parseInt(e)).toList();
            List<Integer> nextNumbers = new ArrayList<>();
            List<Integer> finalNumbers = new ArrayList<>();
            finalNumbers.add(numbers.get(numbers.size()-1));
            while (!numbers.stream().allMatch(e -> e == 0)) {
                for (int i = 1; i < numbers.size(); i++) {
                    nextNumbers.add(numbers.get(i)-numbers.get(i-1));
                }
                finalNumbers.add(nextNumbers.get(nextNumbers.size()-1));
                numbers = new ArrayList<>(nextNumbers);
                nextNumbers.clear();
            }
            int sum = 0;
            for (int i = finalNumbers.size()-1; i >= 0; i--) {
                sum += finalNumbers.get(i);
            }
            finalSum+=sum;
        }
        return finalSum;
    }

    static int solveTask2(List<String> input) {
        int finalSum = 0;
        for (String line : input) {
            List<Integer> numbers =  Arrays.stream(line.split(" ")).map(e -> Integer.parseInt(e)).toList();
            List<Integer> nextRowOfNumbers = new ArrayList<>();
            List<Integer> previousNumbers = new ArrayList<>();
            previousNumbers.add(numbers.get(0));
            while (!numbers.stream().allMatch(e -> e == 0)) {
                for (int i = 1; i < numbers.size(); i++) {
                    nextRowOfNumbers.add(numbers.get(i)-numbers.get(i-1));
                }
                previousNumbers.add(nextRowOfNumbers.get(0));
                numbers = new ArrayList<>(nextRowOfNumbers);
                nextRowOfNumbers.clear();
            }
            int sum = 0;
            for (int i = previousNumbers.size()-1; i > 0; i--) {
                sum = previousNumbers.get(i-1)-sum;
            }
            finalSum+=sum;
        }
        return finalSum;
    }
}
