package de.diedavids.jmix.softreference.cuba.impl;

import de.diedavids.jmix.softreference.cuba.SoftReferenceMigrationService;
import io.jmix.core.Entity;
import io.jmix.core.LoadContext;
import io.jmix.core.Metadata;
import io.jmix.core.MetadataTools;
import io.jmix.core.SaveContext;
import io.jmix.core.Sort;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.entity.EntityValues;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.metamodel.model.MetaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component(SoftReferenceMigrationService.NAME)
public class SoftReferenceMigrationServiceBean implements SoftReferenceMigrationService {

    private static final Logger log = LoggerFactory.getLogger(SoftReferenceMigrationServiceBean.class);

    @Autowired
    UnconstrainedDataManager dataManager;

    @Autowired
    Metadata metadata;
    @Autowired
    MetadataTools metadataTools;

    @Override
    public <T extends Entity> int migrateSoftReferenceAttribute(
            Class<T> entityClass,
            String attributeWithCubaFormat,
            String attributeWithJmixFormat,
            int batchSize
    ) {
        return findPropertyByAnnotation(entityClass, CreatedDate.class.getName())
                .map(createdDateProperty -> migrateSoftReferenceAttribute(entityClass, attributeWithCubaFormat, attributeWithJmixFormat, 1000, createdDateProperty))
                .orElseThrow(() ->
                        new IllegalStateException("No Entity property present with annotation @CreatedDate to sort by. In case you don't have a creation timestamp property in your entity, you need to pass in the property to sort by during migration")
                );
    }

    @Override
    public <T extends Entity> int migrateSoftReferenceAttribute(
            Class<T> entityClass,
            String attributeWithCubaFormat,
            String attributeWithJmixFormat,
            int batchSize,
            String sortProperty
    ) {
        AtomicInteger total = new AtomicInteger(0);

        Batches.doInBatches(
                entityCount(entityClass),
                batchSize,
                batch -> total.getAndAdd(performBatchSlice(batch.getStart(), batchSize, entityClass, attributeWithCubaFormat, attributeWithJmixFormat, sortProperty))
        );
        final List<T> allEntities = dataManager.load(entityClass).all().list();

        allEntities.forEach(it -> convertSoftReferenceAttribute(it, attributeWithCubaFormat, attributeWithJmixFormat));


        final int migratedEntities = total.get();

        log.info("{} entities migrated", migratedEntities);

        return migratedEntities;
    }

    private <T extends Entity> int performBatchSlice(int firstResult, int batchSize, Class<T> entityClass, String attribute, String newAttribute, String sortProperty) {

        log.info("Migrate entities from {} to {}", firstResult, firstResult + batchSize - 1);
        final SaveContext saveContext = new SaveContext();
        dataManager.load(entityClass)
                .all()
                .sort(Sort.by(Sort.Order.asc(sortProperty)))
                .firstResult(firstResult)
                .maxResults(batchSize)
                .list()
                .stream()
                .map(entityInstance -> performConversion(entityInstance, attribute, newAttribute))
                .forEach(saveContext::saving);

        return dataManager.save(saveContext).size();

    }


    protected Optional<String> findPropertyByAnnotation(Class<?> clazz, String annotationName) {
        MetaClass metaClass = metadata.getClass(clazz);

        for (MetaProperty property : metaClass.getProperties()) {
            if (property.getAnnotations().containsKey(annotationName)) {
                return Optional.of(property.getName());
            }
        }

        return Optional.empty();
    }

    private <T extends Entity> T performConversion(T entity, String attribute, String newAttribute) {
        final Object oldValue = EntityValues.getValue(entity, attribute);

        EntityValues.setValue(entity, newAttribute, oldValue);

        return entity;
    }


    private <T extends Entity> int entityCount(Class<T> entityClass) {
        return (int) dataManager.getCount(new LoadContext<>(metadata.getClass(entityClass)));
    }

    private <T extends Entity> void convertSoftReferenceAttribute(T entity, String attribute, String newAttribute) {
        final Object oldValue = EntityValues.getValue(entity, attribute);

        EntityValues.setValue(entity, newAttribute, oldValue);


        dataManager.save(entity);
    }
}
