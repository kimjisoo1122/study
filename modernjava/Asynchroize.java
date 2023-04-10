import java.util.concurrent.*;

public class Asynchroize {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        work1();
        executorService.schedule(Asynchroize::work2, 5, TimeUnit.SECONDS);

        executorService.shutdown();

    }

    public static void work1() {
        System.out.println("WORK1");
    }

    public static void work2() {
        System.out.println("WORK2");
    }
}
