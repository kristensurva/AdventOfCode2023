import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 {
    public static int solveTask1(List<String> input) {
        int sum = 0;
        for (String line : input) {
            StringBuilder numberString = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                if (Character.isDigit(line.charAt(i))) {
                    numberString.append(line.charAt(i));
                }
            }
            //System.out.println(numberString);
            String number = String.valueOf(numberString.charAt(0)) + numberString.charAt(numberString.length() - 1);
            //System.out.println(number);
            sum+= Integer.parseInt(number);
        }
        return sum;
    }

    public static int solveTask2(List<String> input) {
        Pattern pattern = Pattern.compile("(?=(\\d|one|two|three|four|five|six|seven|eight|nine))");
        int sum=0;
        for(String line : input) {
            System.out.println(line);
            StringBuilder stringBuilder = new StringBuilder();
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            char lastDigit = getDigit(matcher.group(1));
            stringBuilder.append(lastDigit);
            while (matcher.find()) {
                lastDigit = getDigit(matcher.group(1));
            }
            stringBuilder.append(lastDigit);
            sum+=Integer.parseInt(stringBuilder.toString());
            System.out.println(stringBuilder);
        }
        return sum;
    }

    private static char getDigit(String string) {
        List<String> digitsAsWords = new ArrayList<>(Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine"));
        if (string.length()==1) {
            return string.charAt(0);
        }
        return Integer.toString(digitsAsWords.indexOf(string)+1).charAt(0);
    }
}
