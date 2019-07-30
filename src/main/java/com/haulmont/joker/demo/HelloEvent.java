package com.haulmont.joker.demo;

import org.springframework.context.ApplicationEvent;

public class HelloEvent extends ApplicationEvent {

    private final String helloString;

    public HelloEvent(Object source, String helloString) {
        super(source);
        this.helloString = helloString;
    }

    public String getHelloString() {
        return helloString;
    }
}
