import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Color{

    private String red;
    private String blue;

    public String getRed() {
        return red;
    }

    public String getBlue() {
        return blue;
    }

    public Color(String red, String blue) {
        this.red = red;
        this.blue = blue;
    }

    public static void main(String[] args) {


        List<Integer> list1 = List.of(1, 2, 3);
        List<Integer> list2 = List.of(3, 4);

        list1.stream().flatMap(e -> list2.stream().map(j -> new int[]{e, j})).collect(Collectors.toList());
        // flatmap은 스트림의 요소를 다른 스트림으로 평면화시키는것




    }

}
