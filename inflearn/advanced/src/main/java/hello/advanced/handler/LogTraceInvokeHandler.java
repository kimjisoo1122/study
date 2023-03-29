package hello.advanced.handler;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
@Slf4j
public class LogTraceInvokeHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace trace;

    public LogTraceInvokeHandler(Object target, LogTrace trace) {
        this.target = target;
        this.trace = trace;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
        log.info(message);
        TraceStatus status = trace.begin(message);
        Object result = method.invoke(target, args);
        trace.end(status);
        return result;
    }
}
