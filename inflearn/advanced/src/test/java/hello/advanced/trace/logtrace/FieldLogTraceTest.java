package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class FieldLogTraceTest {

    @Test
    void begin() {
        FieldLogTrace trace = new FieldLogTrace();
        TraceStatus status1 = trace.begin("Test");
        TraceStatus status2 = trace.begin("Test");
        trace.end(status2);
        trace.end(status1);
    }
}