package com.fastcampus.ch3;

import org.apache.taglibs.standard.tag.common.sql.DataSourceUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnectionTest2 {
    public static void main(String[] args) throws Exception{
        jdbcConnectionTest();
    }
    public static void jdbcConnectionTest() throws Exception {

        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String s = br.readLine();
        System.out.println("s = " + s);
        br.close();


        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/root-context.xml");
        DataSource ds = ac.getBean(DataSource.class);
        Connection conn = ds.getConnection(); //
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT NOW()");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String string = resultSet.getString(1);
            System.out.println("string = " + string);
        }
        System.out.println("conn = " + conn);
    }
}

/* [root-context.xml]
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- Root Context: defines shared resources visible to all other web components -->
	<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
		<property name="url" value="jdbc:mysql://localhost:3306/springbasic?useUnicode=true&amp;characterEncoding=utf8"></property>
		<property name="username" value="asdf"></property>
		<property name="password" value="1234"></property>
	</bean>
</beans>
*/