package com.fastcampus.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
@Aspect
@Component
public class AopMain {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        MyAdvice myAdvice = new MyAdvice();
        MyClass myClass = new MyClass();

        Method[] methods = MyClass.class.getMethods();
        for (Method method : methods) {
            myAdvice.invoke(method, myClass, null);
        }
    }
}

class MyAdvice {


    void invoke(Method method, Object obj,@Nullable Object... args) throws InvocationTargetException, IllegalAccessException {
        System.out.println("[before]");
        method.invoke(obj, args);
        System.out.println("[after]");

    }
}
class MyClass {
    void aaa() {
        System.out.println("aaa() is called");
    }

    void aaa2() {
        System.out.println("aaa2() is called");
    }
    void bbb() {
        System.out.println("bbb() is called");
    }
}
