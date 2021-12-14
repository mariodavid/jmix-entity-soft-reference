package de.diedavids.jmix.jesr.test_support;

import java.util.UUID;

public class FooProvisioning {

    private static final String DEFAULT_NAME = "name";

    static public FooBuilder defaultFooBuilder() {
        return FooBuilder.aFoo()
                .id(UUID.randomUUID())
                .name(DEFAULT_NAME);
    }

    static public Foo defaultFoo() {
        return defaultFooBuilder().build();
    }
}
