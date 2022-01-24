package de.diedavids.jmix.softreference.cuba;

import io.jmix.core.Entity;
import io.jmix.core.metamodel.model.MetaProperty;

/**
 * Services that performs data migration from the CUBA entity representation to the Jmix entity representation.
 *
 * CUBA representation: example$Customer-2fdc4906-fa89-11e7-8c3f-9a214cf093ae (@see {@link com.haulmont.cuba.core.global.EntityLoadInfo}
 * Jmix representation: example$Customer."2fdc4906-fa89-11e7-8c3f-9a214cf093ae" (@see {@link io.jmix.core.IdSerialization}).
 *
 */
public interface SoftReferenceMigrationService {
    String NAME = "softreference_SoftReferenceMigrationService";

    /**
     * migrates all soft references for a given attribute form CUBA to Jmix representation
     *
     * @param <T> type of the entity that holds the soft references
     * @param entityClass the entity type that contains the soft reference attribute to migrate
     * @param attribute the attribute of the entity class that contains the soft references
     * @return true, if all soft references have been migrated, otherwise false
     */
    <T extends Entity> int migrateSoftReferenceAttribute(
            Class<T> entityClass,
            String attribute,
            String newAttribute
    );
    /**
     * migrates all soft references for a given attribute form CUBA to Jmix representation
     *
     * @param <T> type of the entity that holds the soft references
     * @param entityClass the entity type that contains the soft reference attribute to migrate
     * @param attribute the attribute of the entity class that contains the soft references
     * @param sortProperty the attribute to sort by when batching is performed
     * @return true, if all soft references have been migrated, otherwise false
     */
    <T extends Entity> int migrateSoftReferenceAttribute(
            Class<T> entityClass,
            String attribute,
            String newAttribute,
            String sortProperty
    );

}