// do not remove imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

class ArrayUtils {
    // define invert method here
    public static <T> T[] invert(T[] array) {
        List<T> list = new ArrayList<>();
        for (int i = array.length-1; i >= 0; i--) {
            list.add(array[i]);
        }

        return list.toArray(array);
    }
}