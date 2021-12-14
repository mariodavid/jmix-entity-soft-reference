package de.diedavids.jmix.jesr.exception;

public class InvalidEntityReferenceException extends IllegalArgumentException {
    private final String entityReference;

    public InvalidEntityReferenceException(String entityReference, StringIndexOutOfBoundsException e) {
        super(String.format("Invalid Entity Reference: %s", entityReference), e);
        this.entityReference = entityReference;
    }

    public InvalidEntityReferenceException(String entityReference) {
        super(String.format("Invalid Entity Reference: %s", entityReference));
        this.entityReference = entityReference;
    }

    public String getEntityReference() {
        return entityReference;
    }
}
