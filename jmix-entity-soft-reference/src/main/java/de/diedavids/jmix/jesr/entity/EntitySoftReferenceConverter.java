package de.diedavids.jmix.jesr.entity;


import de.diedavids.jmix.jesr.AppBeans;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EntitySoftReferenceConverter implements AttributeConverter<Object, String> {

    public EntitySoftReferenceConverter() {
    }

    @Override
    public String convertToDatabaseColumn(Object entity) {
        return getEntitySoftReference().toEntityReference(entity);
    }

    private EntitySoftReference getEntitySoftReference() {
        return AppBeans.getBean(EntitySoftReference.class);
    }

    @Override
    public Object convertToEntityAttribute(String value) {
        return getEntitySoftReference().toEntity(value);
    }


}
