package tobyspring.helloboot;

import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringBootApplication {
    public static void run(Class<?> clazz, String ... args) {
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext() {
            @Override
            protected void onRefresh() {
                super.onRefresh();
                ServletWebServerFactory webServerFactory = this.getBean(ServletWebServerFactory.class);
                webServerFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet",
                                    this.getBean(DispatcherServlet.class))
                            .addMapping("/*");
                }).start();
            }
        };
        ac.register(clazz);
        ac.refresh();
    }
}
