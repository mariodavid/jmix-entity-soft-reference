package de.diedavids.jmix.softreference.test_support;

import java.util.UUID;

public class DocumentProvisioning {

    private static final String DEFAULT_NAME = "name";

    static public DocumentBuilder defaultDocumentBuilder() {
        return DocumentBuilder.aDocument()
                .id(UUID.randomUUID())
                .name(DEFAULT_NAME);
    }

    static public Document defaultFoo() {
        return defaultDocumentBuilder().build();
    }
}
