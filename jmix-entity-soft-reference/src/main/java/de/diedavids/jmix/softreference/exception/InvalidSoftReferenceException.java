package de.diedavids.jmix.softreference.exception;

public class InvalidSoftReferenceException extends IllegalArgumentException {
    private final String entityReference;

    public InvalidSoftReferenceException(String entityReference, StringIndexOutOfBoundsException e) {
        super(String.format("Invalid Entity Reference: %s", entityReference), e);
        this.entityReference = entityReference;
    }

    public InvalidSoftReferenceException(String entityReference) {
        super(String.format("Invalid Entity Reference: %s", entityReference));
        this.entityReference = entityReference;
    }

    public String getEntityReference() {
        return entityReference;
    }
}
