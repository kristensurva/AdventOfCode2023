import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {
    public static void main(String[] args) throws IOException {
        //System.out.println(Day1.solveTask1(readInFile("Inputs/Day1Test.txt")));
        //System.out.println(Day1.solveTask2(readInFile("Inputs/Day1Input.txt")));
        



        //System.out.println(Day8.solveTask1(readInFile("Inputs/Day8Input.txt")));
        //System.out.println(Day8.solveTask2(readInFile("Inputs/Day8Input.txt")));
    }

    static List<String> readInFile(String path) throws IOException {
        return Files.readAllLines(new File(path).toPath(), Charset.defaultCharset());
    }
}
