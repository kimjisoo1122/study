package hello.advanced.app.v2;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {

    private final HelloTraceV2 trace;

    public void save(TraceStatus status, String itemId) {
        TraceStatus status2 = trace.beginSync(status.getTraceId() ,"OrderRepository.save()");
        if ("ex".equals(itemId)) {
            throw new IllegalStateException("예외 발생!");
        }

        sleep(1000);
        trace.end(status2);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
