package de.diedavids.jmix.jesr.exception;

public class NotExistingEntityReferenceException extends IllegalArgumentException {
    private final String entityReference;

    public NotExistingEntityReferenceException(String entityReference) {
        super(String.format("Not existing Soft Reference: %s", entityReference));
        this.entityReference = entityReference;
    }

    public String getEntityReference() {
        return entityReference;
    }
}
