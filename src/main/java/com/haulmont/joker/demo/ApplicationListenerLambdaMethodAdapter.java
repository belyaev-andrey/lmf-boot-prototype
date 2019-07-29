package com.haulmont.joker.demo;

import org.springframework.context.ApplicationContextException;
import org.springframework.context.event.ApplicationListenerMethodAdapter;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.function.BiFunction;

public class ApplicationListenerLambdaMethodAdapter extends ApplicationListenerMethodAdapter {

    private BiFunction handler;

    public ApplicationListenerLambdaMethodAdapter(String beanName, Class<?> targetClass, Method method) {
        super(beanName, targetClass, method);
        try {
            handler = createListenerHandler(targetClass, method);
        } catch (Throwable throwable) {
            throw new ApplicationContextException("Cannot create handler for a listener method: "+method.getName(), throwable);
        }
    }

    @Override
    protected Object doInvoke(Object... args) {
        Object bean = getTargetBean();
        return handler.apply(bean, args[0]);
    }

    private BiFunction createListenerHandler(Class<?> targetClass, Method method) throws Throwable {
        MethodHandles.Lookup caller = MethodHandles.lookup();
        CallSite site = LambdaMetafactory.metafactory(caller, "apply", MethodType.methodType(BiFunction.class),
                MethodType.methodType(Object.class, Object.class, Object.class),
                caller.findVirtual(
                        targetClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()[0])),
                MethodType.methodType(Object.class, targetClass, method.getParameterTypes()[0]));

        MethodHandle factory = site.getTarget();
        Object invoke = factory.invoke();
        return (BiFunction) invoke;
    }

}
