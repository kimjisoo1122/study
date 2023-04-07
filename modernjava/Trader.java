import javax.lang.model.SourceVersion;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Trader {
    private final String name;
    private final String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Trader : " + this.name + " in " + this.city;
    }

    public static void main(String[] args) {


        Unit marin = Unit.made().name("Marin").hp(30);
        System.out.println(marin);

    }

    static class Unit {

        @Override
        public String toString() {
            return "Unit name : " + name + " hp : " + hp;
        }

        private String name;
        private int hp;

        private static Unit unit;

        public static Unit made() {
            unit = new Unit();
            return unit;
        }

        public Unit name(String name) {
            unit.name = name;
            return unit;
        }
        public Unit hp(int hp) {
            unit.hp = hp;
            return unit;
        }
    }

}



