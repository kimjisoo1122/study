package hello.advanced.trace.template;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;

public abstract class AbstractTemplate<T> {

    private final LogTrace trace;

    public AbstractTemplate(LogTrace logTrace) {
        this.trace = logTrace;
    }

    public T execute(String message) {
        TraceStatus status = null;
        T result;
        try {
            status = trace.begin(message);
            result = call();
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
        return result;
    }

    public abstract T call();
}
