package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;

@SpringBootTest
public class InitTxTest {

    @Autowired
    Hello hello;
    @Test
    void test() {
        Class<Hello> helloClass = Hello.class;
        Method[] declaredMethods = helloClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(Transactional.class)) {
                Transactional annotation = declaredMethod.getAnnotation(Transactional.class);
                System.out.println("annotation = " + Arrays.toString(annotation.label()));
            }
        }

    }

    @TestConfiguration
    static class Config {
        @Bean
        public Hello hello() {
            return new Hello();
        }
    }
@Slf4j
    static class Hello {
        @PostConstruct
        @Transactional
        public void initV1() {
            log.info("@PostConstruct init");
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", actualTransactionActive);
        }


    @Transactional(label = {"test", "sfsafas"})
    public void test() {
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("tx active = {}", actualTransactionActive);
    }
    }

}
