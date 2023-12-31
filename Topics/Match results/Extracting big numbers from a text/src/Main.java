import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String stringWithNumbers = scanner.nextLine();

        // write your code here
        Pattern pattern = Pattern.compile("\\d{10,}");
        Matcher matcher = pattern.matcher(stringWithNumbers);
        while (matcher.find()) {
            System.out.printf("%s:%d\n", matcher.group(), matcher.group().length());
        }
    }
}