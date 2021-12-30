package de.diedavids.jmix.softreference.service;

import de.diedavids.jmix.softreference.SoftReferenceService;
import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import io.jmix.core.querycondition.PropertyCondition;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service(SoftReferenceService.NAME)
public class SoftReferenceServiceBean implements SoftReferenceService {

    private final Metadata metadata;
    private final DataManager dataManager;

    public SoftReferenceServiceBean(Metadata metadata, DataManager dataManager) {
        this.metadata = metadata;
        this.dataManager = dataManager;
    }

    @Override
    public <T, E> boolean doSoftReferencesExist(Class<T> entityClass, E softReference, String attribute) {
        return !loadEntitiesForSoftReference(entityClass, softReference, attribute).isEmpty();
    }

    @Override
    public <T, E> Collection<T> loadEntitiesForSoftReference(Class<T> entityClass, E softReference, String attribute) {
        return loadEntitiesForSoftReference(entityClass, softReference, attribute, null);
    }

    @Override
    public <T, E> int countEntitiesForSoftReference(Class<T> entityClass, E softReference, String attribute) {
        return loadEntitiesForSoftReference(entityClass, softReference, attribute).size();
    }

    @Override
    public <T, E> Collection<T> loadEntitiesForSoftReference(
            Class<T> entityClass,
            E softReference,
            String attribute,
            String fetchPlan
    ) {
        return dataManager.load(entityClass)
                .condition(PropertyCondition.equal(attribute, softReference))
                .fetchPlan(fetchPlan)
                .list();
    }
}