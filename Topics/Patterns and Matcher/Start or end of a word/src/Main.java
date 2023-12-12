import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String part = scanner.nextLine();
        String line = scanner.nextLine();

        // write your code here
        boolean isFound = false;

        Pattern pattern = Pattern.compile(part, Pattern.CASE_INSENSITIVE);
        String[] lines = line.split("[ ,!]");

        for (String lineProcess : lines) {
            Matcher matcher = pattern.matcher(lineProcess);

            if (lineProcess.length() >= part.length()) {
                matcher.region(0, part.length());
                if (matcher.find()) {
                    isFound = true;
                }

                matcher.region(lineProcess.length() - part.length(), lineProcess.length());
                if (matcher.find()) {
                    isFound = true;
                }
            }


        }


        System.out.println( isFound ? "YES" : "NO");
    }
}