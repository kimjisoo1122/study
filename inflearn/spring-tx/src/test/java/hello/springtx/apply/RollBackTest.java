package hello.springtx.apply;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
@SpringBootTest
public class RollBackTest {

    @Autowired
    Exceptions exceptions;
    @Test
    void runtime() {
        try {
            exceptions.run();
        } catch (RuntimeException e) {
            System.out.println("catch run time");
        }
    }


    @TestConfiguration
    static class Config {
        @Bean
        public Exceptions exceptions() {
            return new Exceptions();
        }
    }

    static class Exceptions {
        @Transactional()
        public void run() {
            throw new RuntimeException();
        }
    }

}
