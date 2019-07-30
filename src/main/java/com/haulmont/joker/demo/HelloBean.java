package com.haulmont.joker.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class HelloBean {

    private final String[] helloStrings = {"Hello", "Ola", "Hi", "Привет"};

    private final Random r = new Random();

    @Autowired
    private ApplicationEventPublisher publisher;

    public String sayHello() {
        String helloString = helloStrings[r.nextInt(helloStrings.length)];
        publisher.publishEvent(new HelloEvent(this, helloString));
        return helloString;
    }

}
