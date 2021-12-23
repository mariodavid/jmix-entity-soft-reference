package de.diedavids.jmix.softreference.exception;

public class NotExistingSoftReferenceException extends IllegalArgumentException {
    private final String entityReference;

    public NotExistingSoftReferenceException(String entityReference) {
        super(String.format("Not existing Soft Reference: %s", entityReference));
        this.entityReference = entityReference;
    }

    public String getEntityReference() {
        return entityReference;
    }
}
