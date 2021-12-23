package de.diedavids.jmix.jesr.screen;

import io.jmix.core.Entity;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.core.MetadataTools;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.metamodel.model.MetaPropertyPath;
import io.jmix.core.metamodel.model.Range;
import io.jmix.ui.Actions;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.ComponentGenerationContext;
import io.jmix.ui.component.ComponentGenerationStrategy;
import io.jmix.ui.component.Field;
import io.jmix.ui.component.factory.AbstractComponentGenerationStrategy;
import io.jmix.ui.component.impl.EntityFieldCreationSupport;
import io.jmix.ui.icon.Icons;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import javax.inject.Inject;


@org.springframework.stereotype.Component(SoftReferenceComponentFactory.NAME)
@Order(value = 60)
public class SoftReferenceComponentFactory extends AbstractComponentGenerationStrategy implements ComponentGenerationStrategy {

    public static final String NAME = "jesr_SoftReferenceComponentFactory";

    public SoftReferenceComponentFactory(Messages messages, UiComponents uiComponents, EntityFieldCreationSupport entityFieldCreationSupport, Metadata metadata, MetadataTools metadataTools, Icons icons, Actions actions) {
        super(messages, uiComponents, entityFieldCreationSupport, metadata, metadataTools, icons, actions);
    }

    @Inject
    public void setUiComponents(UiComponents uiComponents) {
        this.uiComponents = uiComponents;
    }


    @Nullable
    @Override
    public Component createComponent(ComponentGenerationContext context) {
        String property = context.getProperty();
        MetaPropertyPath mpp = resolveMetaPropertyPath(context.getMetaClass(), property);

        if (mpp != null) {
            Range mppRange = mpp.getRange();
            if (mppRange.isDatatype()) {
                Class type = mppRange.asDatatype().getJavaClass();
                if (type.equals(Entity.class)) {
                    return createDatatypeLinkField(context);
                }
            }
        }

        return null;
    }

    protected MetaPropertyPath resolveMetaPropertyPath(MetaClass metaClass, String property) {
        MetaPropertyPath mpp = metaClass.getPropertyPath(property);

        /*
        if (mpp == null && DynamicAttributesUtils.isDynamicAttribute(property)) {
            mpp = DynamicAttributesUtils.getMetaPropertyPath(metaClass, property);
        }

         */

        return mpp;
    }

    protected void setDatasource(Field field, ComponentGenerationContext context) {
        if (context.getValueSource() != null && StringUtils.isNotEmpty(context.getProperty())) {
            field.setValueSource(context.getValueSource());
        }
    }
}
