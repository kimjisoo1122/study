package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloServiceTest {

    @Test
    void simpleHelloService() {
        HelloService helloService = new HelloService() {
            @Override
            public String sayHello(String name) {
                return name;
            }
        };
        String name = helloService.sayHello("Spring");
        Assertions.assertThat(name).isEqualTo("Hello Spring");

    }
}
