package de.diedavids.jmix.softreference.entity;


import de.diedavids.jmix.softreference.AppBeans;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SoftReferenceConverter implements AttributeConverter<Object, String> {

    public SoftReferenceConverter() {
    }

    @Override
    public String convertToDatabaseColumn(Object entity) {
        return getEntitySoftReference().softReferenceOf(entity);
    }

    private SoftReferences getEntitySoftReference() {
        return AppBeans.getBean(SoftReferences.class);
    }

    @Override
    public Object convertToEntityAttribute(String value) {
        return getEntitySoftReference().entityOf(value);
    }


}
