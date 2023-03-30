package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* hello.aop.order..*(..))")
    // 장점 모듈화 - 재사용가능
    public void allOrder() {}

    @Pointcut("execution(* *..*Service.*(..))")
    public void allService(){};

    @Pointcut("allOrder() && allService()")
    public void orderAndService(){};
}
