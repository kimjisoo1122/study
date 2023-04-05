package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
public class AspectV5Order {

    private AspectV5Order() {}

    @Order(2)
    @Aspect
    public static class LogAop {
        @Around("hello.aop.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    @Order(1)
    @Aspect
    public static class TxAop {
        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

            try {
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[트랜잭션 종료] {}", joinPoint.getSignature());
                return result;
            } catch (Exception ex) {
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            } finally {
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
            return joinPoint.proceed();
        }
    }
}




