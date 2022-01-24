package de.diedavids.jmix.softreference.cuba.entity;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.*;
import io.jmix.core.Entity;
import io.jmix.core.metamodel.annotation.DatatypeDef;
import io.jmix.core.metamodel.annotation.Ddl;
import io.jmix.core.metamodel.datatype.Datatype;
import io.jmix.core.metamodel.model.MetaClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.Locale;

@DatatypeDef(
        id = "EntitySoftReference",
        javaClass = Entity.class
)
@Ddl("varchar(255)")
public class EntitySoftReferenceDatatype implements Datatype<Entity> {

    @Override
    public Class getJavaClass() {
        return Entity.class;
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value) {

        if (value == null) {
            return "";
        }

        EntityLoadInfoBuilder builder = getEntityLoadInfoBuilder();
        EntityLoadInfo entityLoadInfo = builder.create((Entity) value);
        return entityLoadInfo.toString();

    }

    private EntityLoadInfoBuilder getEntityLoadInfoBuilder() {
        return AppBeans.get(EntityLoadInfoBuilder.NAME);
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public Entity parse(@Nullable String value) throws ParseException {


        if (Strings.isNullOrEmpty(value))
            return null;


        EntityLoadInfoBuilder builder = getEntityLoadInfoBuilder();
        EntityLoadInfo entityLoadInfo = builder.parse(value);

        Entity entity = loadEntity(entityLoadInfo);

        return entity;
    }

    private Entity loadEntity(EntityLoadInfo entityLoadInfo) {
        DataManager dataManager = getDataManager();
        return dataManager.load(getLoadContextForForEntityLoadInfo(entityLoadInfo.getMetaClass(), entityLoadInfo.getId()));
    }

    private DataManager getDataManager() {
        return AppBeans.get(DataManager.NAME);
    }


    protected LoadContext getLoadContextForForEntityLoadInfo(MetaClass metaClass, Object entityId) {
        LoadContext loadContext = LoadContext.create(metaClass.getJavaClass());
        loadContext
                .setId(entityId);
        return loadContext;
    }

    @Nullable
    @Override
    public Entity parse(@Nullable String value, Locale locale) throws ParseException {
        return parse(value);
    }

}
