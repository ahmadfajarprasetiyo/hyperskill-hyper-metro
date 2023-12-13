import java.io.IOException;

class Converter {
    public static char[] convert(String[] words) throws IOException {
        String res = "";
        // implement the method
        for (String word : words) {
            res = res.concat(word);
        }

        return res.toCharArray();
    }
}