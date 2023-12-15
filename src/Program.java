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
        System.out.println(Day2.solveTask2(readInFile("Inputs/Day2Input.txt")));

        //System.out.println(Day8.solveTask1(readInFile("Inputs/Day8Input.txt")));
        //System.out.println(Day8.solveTask2(readInFile("Inputs/Day8Input.txt")));
    }

    static List<String> readInFile(String path) throws IOException {
        return Files.readAllLines(new File(path).toPath(), Charset.defaultCharset());
    }
}
