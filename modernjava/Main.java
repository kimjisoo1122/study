import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        Optional<Shop> optionalShop = Optional.empty();
        Shop shop = optionalShop.orElseGet(() -> null);
        System.out.println("shop = " + shop);
//        optionalShop.ifPresent(System.out::println);

        getTemperatures("New York").subscribe(new TempInfo.TempSubscriber());

        List<Shop> list = new ArrayList<>();
        list.stream().sorted(Comparator.nullsFirst(Comparator.comparing(Shop::getProduct)));

        DoubleUnaryOperator doubleUnaryOperator = curriedConverter(3, 6);
        double v = doubleUnaryOperator.applyAsDouble(2);
    }

    public static DoubleUnaryOperator curriedConverter(double a, double b) {
        return x -> x * a + b;
    }

    private static Flow.Publisher<TempInfo> getTemperatures(String town) {
        return subscriber -> subscriber.onSubscribe(new TempInfo.TempSubscription(subscriber, town));
    }
}
