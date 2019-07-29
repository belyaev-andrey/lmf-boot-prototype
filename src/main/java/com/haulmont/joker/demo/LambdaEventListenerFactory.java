package com.haulmont.joker.demo;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.DefaultEventListenerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class LambdaEventListenerFactory extends DefaultEventListenerFactory {

    @Override
    public int getOrder() {
        return super.getOrder()-1;
    }

    @Override
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        return new ApplicationListenerLambdaMethodAdapter(beanName, type, method);
    }


}
