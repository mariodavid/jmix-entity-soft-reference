package de.diedavids.jmix.softreference;

import java.util.Collection;


/**
 * Ability to get information about soft references
 */
public interface SoftReferenceService {
    String NAME = "softreference_SoftReferenceService";

    /**
     * checks if an entity type has a soft reference to a given soft reference
     * @param entityClass the entity type to check for soft references against
     * @param softReference the soft reference to check
     * @param attribute the attribute of the entity class that contains the soft reference
     * @param <T> type of the entity to check for soft references
     * @param <E> type of the entity of the soft reference
     * @return true, if the entity class contains an entry with this soft reference, otherwise false
     */
    <T, E> boolean doSoftReferencesExist(
            Class<T> entityClass,
            E softReference,
            String attribute
    );

    /**
     * loads entities for a given entity class that use the soft reference
     * @param entityClass the entity type to check for soft references against
     * @param softReference the soft reference to check
     * @param attribute the attribute of the entity class that contains the soft reference
     * @param <T> type of the entity to check for soft references
     * @param <E> type of the entity of the soft reference
     * @return a collection of entities that match this soft reference
     */
    <T, E> Collection<T> loadEntitiesForSoftReference(Class<T> entityClass, E softReference, String attribute);

    /**
     * loads entities for a given entity class that use the soft reference
     * @param entityClass the entity type to check for soft references against
     * @param softReference the soft reference to check
     * @param attribute the attribute of the entity class that contains the soft reference
     * @param fetchPlan the fetch plan to load the entities with
     * @param <T> type of the entity to check for soft references
     * @param <E> type of the entity of the soft reference
     * @return a collection of entities that match this soft reference
     */
    <T, E> Collection<T> loadEntitiesForSoftReference(Class<T> entityClass, E softReference, String attribute, String fetchPlan);

    /**
     * counts entities for a given entity class that use the soft reference
     * @param entityClass the entity type to check for soft references against
     * @param softReference the soft reference to check
     * @param attribute the attribute of the entity class that contains the soft reference
     * @param <T> type of the entity to check for soft references
     * @param <E> type of the entity of the soft reference
     * @return the amount of entities that match this soft reference
     */
    <T, E> int countEntitiesForSoftReference(Class<T> entityClass, E softReference, String attribute);
}