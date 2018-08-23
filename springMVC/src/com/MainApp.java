package com;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class MainApp {
    public static void main(String[] args) {
        XmlBeanFactory factory = new XmlBeanFactory
                (new ClassPathResource("Beans.xml"));
        HelloWorld obj = (HelloWorld) factory.getBean("helloWorld");
//        ApplicationContext context =
//                new ClassPathXmlApplicationContext("Beans.xml");
//        HelloWorld obj = (HelloWorld) context.getBean("helloWorld");
        obj.getMessage();
        HelloWorld obj1 = (HelloWorld) factory.getBean("helloWorld");
        System.out.println(obj==obj1);

    }
}
