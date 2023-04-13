package com.fastcampus.ch3.diCopy1;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

class Car {}
class SportsCar extends Car {}
class Truck extends Car {}
@Component
public class Main {

    private static final Map<String, Object> applicationContext = new HashMap();

    public static void main(String[] args) throws Exception {

        getClassesInPackage("com.fastcampus.ch3.diCopy1");

        applicationContext.forEach((k, v) -> System.out.println("k = " + k + " v = " + v));

    }

    public static Car getCar(String carType) throws Exception {
        Properties p = new Properties();
        Class clazz = null;
        try (FileReader fileReader = new FileReader(new File("config.txt"))) {
            p.load(fileReader);
            clazz = Class.forName(p.getProperty(carType));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return (Car) clazz.newInstance();
    }

    public static void getClassesInPackage(String packageName) throws Exception {
        String path = packageName.replace(".", "/");

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URL resource = contextClassLoader.getResource(path);
        File directory = new File(resource.getFile());
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);

                Class<?> clazz = Class.forName(className);
                Component component = clazz.getAnnotation(Component.class);
                if (component != null) {
                    char[] chars = clazz.getSimpleName().toCharArray();
                    chars[0] = Character.toLowerCase(chars[0]);
                    String beanName = new String(chars);
                    applicationContext.put(beanName, clazz.newInstance());
                }
            } else if (file.isDirectory()) {
                getClassesInPackage(packageName + "." + file.getName());
            }
        }
    }
}
