import com.google.common.base.Strings;
import org.junit.Test;

import java.util.Arrays;

public class StringT {
    public static void main(String[] args) {
        String s = "1,28829,1526263901458,,,,39.78,84.01,,,,21.48,,89.13,92.13,,,,17.95,76.24,52.93";
        String[] split = s.split(",");
        System.out.println(Arrays.toString(split));
        System.out.println(split[3].equals(""));

    }

    @Test
    public void test(){
        String[] a ={"a","b","c"};
        System.out.println(Arrays.toString(Arrays.copyOfRange(a,1,a.length)));
    }
}
