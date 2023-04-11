public class TestThread {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Thread : " + Thread.currentThread().getName());

        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread : " + Thread.currentThread().getName());
        });
        thread1.setDaemon(true);
        thread1.start();
        thread1.join();
    }
}
