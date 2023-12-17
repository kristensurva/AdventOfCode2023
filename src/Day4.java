import java.util.*;

public class Day4 {
    public static int solveTask1(List<String> input) {
        int sum = 0;
        for (String line : input) {
            String[] temp = line.split(":|\\|");
            List<String> winningNumbers = new ArrayList<>(List.of(temp[1].trim().split("\\s+")));
            List<String> candidateNumbers = new ArrayList<>(List.of(temp[2].trim().split("\\s+")));
            System.out.println(winningNumbers);
            System.out.println(candidateNumbers);
            int amountOfWinningNumbers = 0;
            for (String candidateNumber : candidateNumbers) {
                if (winningNumbers.contains(candidateNumber)) {
                    amountOfWinningNumbers++;
                }
            }
            System.out.println("Amount of winning numbers: " + amountOfWinningNumbers);
            System.out.println(Math.pow(2, amountOfWinningNumbers-1));
            sum+=Math.pow(2, amountOfWinningNumbers-1);
            System.out.println("---");
        }
        return sum;
    }

    // --- 2nd part ---
    static Queue<Integer> wonCards = new LinkedList<>();
    static HashMap<Integer, List<String>> winningNumbersByGameID = new HashMap<>();
    static HashMap<Integer, List<String>> candidateNumbersByGameID = new HashMap<>();

    public static int solveTask2(List<String> input) {
        int sum = 0;
        // First pass, original cards
        for (String line : input) {
            String[] temp = line.split(":|\\|");
            int gameID = Integer.parseInt(temp[0].substring(4).trim());
            List<String> winningNumbers = new ArrayList<>(List.of(temp[1].trim().split("\\s+")));
            List<String> candidateNumbers = new ArrayList<>(List.of(temp[2].trim().split("\\s+")));
            processGame(gameID, winningNumbers, candidateNumbers);
            sum++;
        }
        // Second pass, for all the copies
        while (!wonCards.isEmpty()) {
            int gameID = wonCards.poll();
            if (gameID<=winningNumbersByGameID.size()) {
                List<String> winningNumbers = winningNumbersByGameID.get(gameID);
                List<String> candidateNumbers = candidateNumbersByGameID.get(gameID);
                processGame(gameID, winningNumbers, candidateNumbers);
                sum++;
            }
        }
        return sum;
    }

    static void processGame(int gameID, List<String> winningNumbers, List<String> candidateNumbers) {
        if (!winningNumbersByGameID.containsKey(gameID)) {
            winningNumbersByGameID.put(gameID, winningNumbers);
            candidateNumbersByGameID.put(gameID, candidateNumbers);
        }
        System.out.println(winningNumbers);
        System.out.println(candidateNumbers);

        int amountOfWinningNumbers = 0;
        for (String candidateNumber : candidateNumbers) {
            if (winningNumbers.contains(candidateNumber)) {
                amountOfWinningNumbers++;
            }
        }
        for (int i = 1; i <= amountOfWinningNumbers; i++) {
            wonCards.add(gameID+i);
        }
        System.out.println("Amount of winning numbers: " + amountOfWinningNumbers);
        System.out.println("---");
    }
}