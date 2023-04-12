package com.example.springmvc.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAdvice {

    @Around("execution(* com.example.springmvc..*.*(..))")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println(proceedingJoinPoint);
        return proceedingJoinPoint.proceed();
    }

//    @Around("execution(* com.example.springmvc.controller.TestRequestParam.hello(..)) && args(num, ..)")
//    public Object log(ProceedingJoinPoint proceedingJoinPoint, int num) throws Throwable {
//        System.out.println(proceedingJoinPoint);
//        Object proceed = proceedingJoinPoint.proceed();
//        return proceed;
//    }
}
