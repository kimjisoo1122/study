package tobyspring.helloboot;

import tobyspring.config.MySpringBootApplication;

@MySpringBootApplication
public class HellobootApplication {
    public static void main(String[] args) {
        MySpringBootApplicationOld.run(HellobootApplication.class, args);
    }
}
