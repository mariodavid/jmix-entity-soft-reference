package de.diedavids.jmix.jesr.entity;

import io.jmix.core.Entity;
import io.jmix.core.metamodel.annotation.DatatypeDef;
import io.jmix.core.metamodel.annotation.Ddl;
import io.jmix.core.metamodel.datatype.Datatype;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;


@DatatypeDef(
        id = "EntitySoftReference",
        javaClass = Entity.class
)
@Ddl("varchar(255)")
public class EntitySoftReferenceDatatype implements Datatype<Entity> {


    /**
     * workaround to inject application context to retrieve IdSerialization
     * until direct injection of IdSerialization is supported
     *
     * see: https://github.com/Haulmont/jmix-core/issues/272
     */
    private final ApplicationContext applicationContext;

    public EntitySoftReferenceDatatype(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class getJavaClass() {
        return Entity.class;
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value) {
        return entitySoftReference().toEntityReference(value);
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public Entity parse(@Nullable String value) {
        return entitySoftReference().toEntity(value);
    }

    @Nullable
    @Override
    public Entity parse(@Nullable String value, Locale locale) {
        return parse(value);
    }

    private EntitySoftReference entitySoftReference() {
        return applicationContext.getBean(EntitySoftReference.class);
    }
}
