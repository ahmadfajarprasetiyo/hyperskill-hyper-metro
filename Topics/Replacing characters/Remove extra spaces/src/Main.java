import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RemoveExtraSpacesProblem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        // write your code here

        Pattern pattern = Pattern.compile("\\s+");

        Matcher matcher = pattern.matcher(text);

        System.out.println(matcher.replaceAll(" "));
    }
}