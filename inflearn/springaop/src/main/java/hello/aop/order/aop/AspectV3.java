package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

    @Pointcut("execution(* hello.aop.order..*(..))")
    // 장점 모듈화 - 재사용가능
    private void allOrder() {
    }


    @Pointcut("execution(* *..*Service.*(..))")
    private void allService(){};

    @Around("allOrder() && allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[트랜잭션 종료] {}", joinPoint.getSignature());
        } catch (Exception ex) {
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
        } finally {
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
        return joinPoint.proceed();
    }
}
