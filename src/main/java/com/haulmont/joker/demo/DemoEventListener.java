package com.haulmont.joker.demo;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DemoEventListener {

    @EventListener
    public String handleContextStart(ApplicationStartedEvent appStartedEvent) {
        System.out.println("Application event handled: "+appStartedEvent.getTimestamp());
        return appStartedEvent.toString();
    }

}
