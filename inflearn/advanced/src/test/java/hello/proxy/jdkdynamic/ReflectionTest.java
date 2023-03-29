package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA();
        log.info("resulut1 = {}", result1);

        log.info("start");
        String result2 = target.callB();
        log.info("resulut2 = {}", result2);
    }

    @Test
    void reflection1() throws Exception {
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    }


    @Slf4j
    // slf4j는 private staic final log를 컴파일 시점에서 추가해줌.
    // 내부 클래스에서 static을 사용할 수 없어서 컴파일 에러가 뜸
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}

