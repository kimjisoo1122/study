import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Shop {
    private String product;
    private double price;

    private enum Code {
        GOLD(10), SILVE(20);

        private int percentage;
        Code(int percentage) {
            this.percentage = percentage;
        }
    }


    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public Shop(String product) {
        this.product = product;
        this.price = new Random().nextDouble() + product.charAt(0) + product.charAt(1);
    }

    public static void main(String[] args) {
        List<Shop> list = List.of(
                new Shop("Best Price1"),
                new Shop("Best Price2"),
                new Shop("Best Price3"),
                new Shop("Best Price4"),
                new Shop("Best Price5"),
                new Shop("Best Price6"),
                new Shop("Best Price7"),
                new Shop("Best Price8"),
                new Shop("Best Price9"),
                new Shop("Best Price10"),
                new Shop("Best Price11"),
                new Shop("Good Price4")
        );

        long start = System.currentTimeMillis();
//        List<String> collect =
//                list.parallelStream()
//                        .map(e -> calculatePirce(e.getProduct(), e.getPrice()))
//                        .collect(Collectors.toList());

        List<CompletableFuture<String>> collect = list.stream()
                .map(e -> CompletableFuture.supplyAsync(() -> calculatePirce(e.getProduct(), e.getPrice())))
                .collect(Collectors.toList());

        List<String> collect1 = collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
        System.out.println("System.currentTimeMillis() - start = " + (System.currentTimeMillis() - start));
        System.out.println("collect = " + collect1);

        int i = Runtime.getRuntime().availableProcessors();
        System.out.println("i = " + i);


    }

    public static String calculatePirce(String product, double price)  {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        return String.format("%s is %.2f price", product, price);
    }
}
