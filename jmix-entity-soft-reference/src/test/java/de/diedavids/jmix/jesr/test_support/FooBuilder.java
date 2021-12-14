package de.diedavids.jmix.jesr.test_support;

import java.util.UUID;

public final class FooBuilder {
    private String name;
    private UUID id;

    private FooBuilder() {
    }

    public static FooBuilder aFoo() {
        return new FooBuilder();
    }

    public FooBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public FooBuilder name(String name) {
        this.name = name;
        return this;
    }

    public Foo build() {
        Foo foo = new Foo();
        foo.setId(id);
        foo.setName(name);
        return foo;
    }
}
