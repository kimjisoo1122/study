import java.util.List;
import java.util.concurrent.*;

public class TestExecutorService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread : " + getThreadName());
            return "Hello";
        });
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread : " + getThreadName());
            return "World";
        });

        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread : " + getThreadName());
            return 1122;
        });

        List<String> k = List.of("k", "k");


        System.out.println(String.join(" ", helloFuture.join(), worldFuture.join()));

    }

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }
}
