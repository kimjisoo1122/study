import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        Color color = new Color("1", "2");
        List<Color> list = new ArrayList<>();
        list.add(color);
        list.sort(Comparator.comparing(Color::getBlue).reversed().thenComparing(Color::getRed));

        Function<Color, String> f = Color::getBlue;

    }

}
