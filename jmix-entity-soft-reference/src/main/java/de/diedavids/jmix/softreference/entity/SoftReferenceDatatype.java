package de.diedavids.jmix.softreference.entity;

import io.jmix.core.Entity;
import io.jmix.core.metamodel.annotation.DatatypeDef;
import io.jmix.core.metamodel.annotation.Ddl;
import io.jmix.core.metamodel.datatype.Datatype;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;


@DatatypeDef(
        id = "SoftReference",
        javaClass = Object.class
)
@Ddl("varchar(255)")
public class SoftReferenceDatatype implements Datatype<Object> {

    /**
     * workaround to inject application context to retrieve IdSerialization
     * until direct injection of IdSerialization is supported
     *
     * see: https://github.com/Haulmont/jmix-core/issues/272
     */
    private final ApplicationContext applicationContext;

    public SoftReferenceDatatype(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class getJavaClass() {
        return Entity.class;
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value) {
        return entitySoftReference().softReferenceOf(value);
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public Object parse(@Nullable String value) {
        return entitySoftReference().entityOf(value);
    }

    @Nullable
    @Override
    public Object parse(@Nullable String value, Locale locale) {
        return parse(value);
    }

    private SoftReferences entitySoftReference() {
        return applicationContext.getBean(SoftReferences.class);
    }
}
