import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
public class Program {
    public static void main(String[] args) throws IOException {
        //System.out.println(Day1.solveTask1(readInFile("Inputs/Day1Test.txt")));
        //System.out.println(Day1.solveTask2(readInFile("Inputs/Day1Input.txt")));
        //System.out.println(Day2.solveTask1(readInFile("Inputs/Day2Input.txt")));
        //System.out.println(Day2.solveTask2(readInFile("Inputs/Day2Input.txt")));
        //System.out.println(Day3.solveTask1(readInFile("Inputs/Day3Input.txt")));
        //System.out.println(Day3.solveTask2(readInFile("Inputs/Day3Input.txt")));
        //System.out.println(Day4.solveTask1(readInFile("Inputs/Day4Input.txt")));
        //System.out.println(Day4.solveTask2(readInFile("Inputs/Day4Input.txt")));
        //System.out.println(Day5.solveTask1(readInFile("Inputs/Day5Input.txt")));
        //System.out.println(Day5.solveTask2(readInFile("Inputs/Day5Input.txt")));
        //System.out.println(Day6.solveTask1(readInFile("Inputs/Day6Input.txt")));
        //System.out.println(Day6.solveTask2(readInFile("Inputs/Day6Input.txt")));
        //System.out.println(Day7.solveTask1(readInFile("Inputs/Day7Input.txt")));
        //System.out.println(Day7.solveTask2(readInFile("Inputs/Day7Input.txt")));
        //System.out.println(Day8.solveTask1(readInFile("Inputs/Day8Input.txt")));
        //System.out.println(Day8.solveTask2(readInFile("Inputs/Day8Input.txt")));
        //System.out.println(Day9.solveTask1(readInFile("Inputs/Day9Input.txt")));
        //System.out.println(Day9.solveTask2(readInFile("Inputs/Day9Input.txt")));
        //System.out.println(Day10.solveTask1(readInFile("Inputs/Day10Test.txt")));
        //System.out.println(((3+1)+4)%4);
        System.out.println(Day10.solveTask1(readInFile("Inputs/Day10Input.txt")));
        System.out.println(Day10.solveTask2(readInFile("Inputs/Day10Input.txt")));


    }

    static List<String> readInFile(String path) throws IOException {
        return Files.readAllLines(new File(path).toPath(), Charset.defaultCharset());
    }
}
