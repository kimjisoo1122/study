import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Apple {

    public static void prettyPrintApple(List<Apple> inventory, Function<Apple, String> function) {
        for (Apple apple : inventory) {
            String output = function.apply(apple);
            System.out.println(output);
        }
    }

    @Override
    public String toString() {
        return "color : " + color;
    }

    private String color;
    private int weight;

    public Apple(String color,int weight) {
        this.color = color;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    public static boolean isGreenApple(Apple apple) {
        return "GREEN".equals(apple.getColor());
    }

    public static boolean isRedApple(Apple apple) {
        return "RED".equals(apple.getColor());
    }

    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> predicate) {


        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static void main(String[] args) {

        List<Apple> inventory = new ArrayList<>();
        inventory.add(new Apple("RED",150));
        inventory.add(new Apple("RED", 200));
        inventory.add(new Apple("RED", 50));
        inventory.add(new Apple("GREEN", 70));

        inventory.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getColor().compareTo(o2.getColor());
            }
        });

        prettyPrintApple(inventory, e -> {
            StringBuilder sb = new StringBuilder();
            if (e.getWeight() < 150) {
                sb.append("light ");
            } else {
                sb.append("heavy ");
            }
            sb.append(e.getColor());
            return sb.toString();
        });
    }
}
