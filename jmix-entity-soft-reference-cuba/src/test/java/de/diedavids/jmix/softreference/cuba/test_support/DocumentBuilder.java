package de.diedavids.jmix.softreference.cuba.test_support;

import java.util.UUID;

public final class DocumentBuilder {
    private String name;
    private UUID id;

    private DocumentBuilder() {
    }

    public static DocumentBuilder aDocument() {
        return new DocumentBuilder();
    }

    public DocumentBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public DocumentBuilder name(String name) {
        this.name = name;
        return this;
    }

    public Document build() {
        Document document = new Document();
        document.setId(id);
        document.setName(name);
        return document;
    }
}
