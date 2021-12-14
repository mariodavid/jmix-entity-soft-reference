package de.diedavids.jmix.jesr.entity;

import com.google.common.base.Strings;
import de.diedavids.jmix.jesr.exception.InvalidEntityReferenceException;
import de.diedavids.jmix.jesr.exception.NotExistingEntityReferenceException;
import io.jmix.core.Entity;
import io.jmix.core.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EntitySoftReferenceConverter implements AttributeConverter<Entity, String> {

    private final EntitySoftReference entitySoftReference;

    public EntitySoftReferenceConverter(EntitySoftReference entitySoftReference) {
        this.entitySoftReference = entitySoftReference;
    }

    @Override
    public String convertToDatabaseColumn(Entity entity) {
        return entitySoftReference.toEntityReference(entity);
    }

    @Override
    public Entity convertToEntityAttribute(String value) {
        return entitySoftReference.toEntity(value);
    }

}
