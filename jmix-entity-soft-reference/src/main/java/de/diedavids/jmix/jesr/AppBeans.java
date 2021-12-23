package de.diedavids.jmix.jesr;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

public class AppBeans {

    private AppBeans() {}

    private static ApplicationContext applicationContext;

    private static void setApplicationContext(ApplicationContext applicationContext) {
        AppBeans.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    @Component
    static class ApplicationContextRetriever {

        @EventListener
        public void storeApplicationContext(ContextRefreshedEvent event) {
            AppBeans.setApplicationContext(event.getApplicationContext());
        }
    }
}