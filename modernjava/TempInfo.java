import java.util.Random;
import java.util.concurrent.Flow;

public class TempInfo {
    public static final Random random = new Random();

    private final String town;
    private final int temp;

    public TempInfo(String town, int temp) {
        this.town = town;
        this.temp = temp;
    }

    public static TempInfo fetch(String town) {
        if (random.nextInt(10) == 0) {
            throw new RuntimeException("Error!");
        }
        return new TempInfo(town, random.nextInt(100));
    }

    public String getTown() {
        return town;
    }

    public int getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        return town + " : " + temp;
    }

    static class TempSubscription implements Flow.Subscription {

        private final Flow.Subscriber<? super TempInfo> subscriber;
        private final String town;

        public TempSubscription(Flow.Subscriber<? super TempInfo> subscriber, String town) {
            this.subscriber = subscriber;
            this.town = town;
        }

        @Override
        public void request(long n) {
            for (long i = 0L; i < n; i++) {
                try {
                    subscriber.onNext(TempInfo.fetch(town));
                } catch (Exception exception) {
                    subscriber.onError(exception);
                    break;
                }
            }
        }

        @Override
        public void cancel() {
            subscriber.onComplete();
        }
    }

    static class TempSubscriber implements Flow.Subscriber<TempInfo> {

        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
        }

        @Override
        public void onNext(TempInfo tempInfo) {
            System.out.println("tempInfo = " + tempInfo);
            subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            System.err.println(throwable.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("Done!");
        }
    }

}
