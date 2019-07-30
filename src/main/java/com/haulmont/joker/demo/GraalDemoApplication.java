package com.haulmont.joker.demo;


import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Supplier;

public class GraalDemoApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(GraalDemoApplication.class) {
			@Override
			protected void load(ApplicationContext context, Object[] sources) {
				// Disable the annotation bean definition reader because of https://github.com/oracle/graal/issues/630
			}
		};
		application.setWebApplicationType(WebApplicationType.REACTIVE);
		application.setApplicationContextClass(ReactiveWebServerApplicationContext.class);

		// Only functional registration is supported by now because of https://github.com/oracle/graal/issues/630
		application.addInitializers((GenericApplicationContext context) -> {
			context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
			context.registerBean(HttpHandler.class, () -> {
				HandlerStrategies strategies = HandlerStrategies.withDefaults();
                HelloBean bean = context.getBean(HelloBean.class);
                RouterFunction<ServerResponse> router = RouterFunctions.route(
                        RequestPredicates.GET("/"),
                        serverRequest -> ServerResponse.ok().syncBody(bean.sayHello()+" Graal"));
				return RouterFunctions.toHttpHandler(router, strategies);
			});
			context.registerBean(ReactiveWebServerFactory.class, (Supplier<ReactiveWebServerFactory>) NettyReactiveWebServerFactory::new);
		});
		application.addInitializers((GenericApplicationContext context) -> {
            context.registerBean(DemoEventListener.class);
            context.registerBean(HelloBean.class);
            context.registerBean(LambdaEventListenerFactory.class);
            AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
        });
		application.run(args);
	}
}
