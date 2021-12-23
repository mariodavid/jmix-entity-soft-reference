package de.diedavids.jmix.jesr.screen;

import io.jmix.core.MessageTools;
import io.jmix.ui.component.ComponentGenerationContext;
import io.jmix.ui.component.Field;
import io.jmix.ui.component.Form;
import io.jmix.ui.component.UiComponentsGenerator;
import io.jmix.ui.component.data.value.ContainerValueSource;
import io.jmix.ui.model.InstanceContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper class that generates a entity soft reference into a form component
 *
 * Usage:
 *
 * Inside an Editor the EntitySoftReferenceFormFieldGenerator bean can be injected. In the initialization
 * phase of the editor, the bean can be used to add the soft reference form field into the form
 *
 *   @Autowired
 *   private EntitySoftReferenceFormFieldGenerator entitySoftReferenceFormFieldGenerator;
 *
 *   @Subscribe
 *   protected void onInit(InitEvent event) {
 *
 *     entitySoftReferenceFormFieldGenerator.initSoftReferenceFormField(form, "mySoftReferenceFieldName", instanceContainer);
 *
 *   }
 *
 */
@Component(SoftReferenceFormFieldGenerator.NAME)
public class SoftReferenceFormFieldGenerator {

    public static final String NAME = "jesr_SoftReferenceFormFieldGenerator";

    @Autowired
    protected UiComponentsGenerator uiComponentsGenerator;

    @Autowired
    protected MessageTools messageTools;


    /**
     * initialized the soft reference as a form field
     * @param form the destination form component instance
     * @param container the instance container
     * @param property the soft reference property
     */
    public void initSoftReferenceFormField(Form form, InstanceContainer container, String property) {

        Field field = generateSoftReferenceField(property, container);

        setCaption(property, container, field);
        setValueSource(property, container, field);

        form.add(field);
    }

    private void setValueSource(String property, InstanceContainer container, Field field) {
        field.setValueSource(new ContainerValueSource<>(container, property));
    }

    private void setCaption(String property, InstanceContainer container, Field field) {
        String propertyCaption = messageTools.getPropertyCaption(container.getEntityMetaClass(), property);
        field.setCaption(propertyCaption);
    }

    private Field generateSoftReferenceField(String property, InstanceContainer container) {
        return (Field) uiComponentsGenerator.generate(new ComponentGenerationContext(container.getEntityMetaClass(), property));
    }
}