package de.diedavids.jmix.jesr.screen;

import io.jmix.core.MetadataTools;
import io.jmix.core.entity.EntityValues;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.LinkButton;
import io.jmix.ui.component.Table;
import io.jmix.ui.screen.FrameOwner;
import io.jmix.ui.screen.Screen;

import javax.annotation.Nullable;

/**
 * Helper class that generates an entity soft reference as a generated table column
 *
 * Usage:
 *
 * Within a controller a table can add a generated column with a new instance of a
 * EntitySoftReferenceTableColumnGenerator.
 *
 * @Subscribe
 * protected void onInit(InitEvent event) {
 *  myTable.addGeneratedColumn("myEntitySoftReferenceColumnName",
 *      new SoftReferenceInstanceNameTableColumnGenerator(
 *      "myEntitySoftReferenceColumnName",
 *      uiComponents,
 *      metadataTools,
 *      screenBuilders,
 *      this
 *  ));
 * }
 */
public class SoftReferenceInstanceNameTableColumnGenerator<EntityWithSoftReference, SoftReferenceTarget> implements Table.ColumnGenerator<EntityWithSoftReference> {

    private final String property;

    private final UiComponents uiComponents;
    private final MetadataTools metadataTools;
    private final ScreenBuilders screenBuilders;
    private final FrameOwner frameOwner;

    /**
     * Creates a new SoftReferenceInstanceNameTableColumnGenerator
     * @param property the entity soft reference property name
     * @param uiComponents instance of CUBAs UiComponents bean
     * @param metadataTools instance of CUBAs metadataTools bean
     * @param screenBuilders instance of CUBAs screenBuilders bean
     * @param frameOwner the frame owner that renders the frame
     */
    public SoftReferenceInstanceNameTableColumnGenerator(String property, UiComponents uiComponents, MetadataTools metadataTools, ScreenBuilders screenBuilders, FrameOwner frameOwner) {
        this.property = property;
        this.uiComponents = uiComponents;
        this.metadataTools = metadataTools;
        this.screenBuilders = screenBuilders;
        this.frameOwner = frameOwner;
    }


    @Nullable
    @Override
    public Component generateCell(EntityWithSoftReference entity) {

        SoftReferenceTarget softReference = getSoftReference(entity);
        return softReferenceLinkButton(softReference);
    }

    private LinkButton softReferenceLinkButton(SoftReferenceTarget entitySoftReference) {

        if (entitySoftReference == null) {
            return null;
        }

        LinkButton linkButton = uiComponents.create(LinkButton.class);

        linkButton.setCaption(metadataTools.getInstanceName(entitySoftReference));

        linkButton.addClickListener(clickEvent ->
                softReferenceEditor(entitySoftReference).show()
        );
        return linkButton;
    }

    private SoftReferenceTarget getSoftReference(EntityWithSoftReference entity) {
        return EntityValues.getValue(entity, property);
    }

    private Screen softReferenceEditor(SoftReferenceTarget softReference) {
        return screenBuilders.editor((Class<SoftReferenceTarget>) softReference.getClass(), frameOwner)
                .editEntity(softReference)
                .build();
    }

}