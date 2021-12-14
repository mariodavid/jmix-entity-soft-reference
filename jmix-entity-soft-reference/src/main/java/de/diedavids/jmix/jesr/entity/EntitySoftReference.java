package de.diedavids.jmix.jesr.entity;

import com.google.common.base.Strings;
import de.diedavids.jmix.jesr.exception.InvalidEntityReferenceException;
import de.diedavids.jmix.jesr.exception.NotExistingEntityReferenceException;
import io.jmix.core.DataManager;
import io.jmix.core.Entity;
import io.jmix.core.Id;
import io.jmix.core.IdSerialization;
import io.jmix.core.metamodel.annotation.DatatypeDef;
import io.jmix.core.metamodel.annotation.Ddl;
import io.jmix.core.metamodel.datatype.Datatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;


@Component
public class EntitySoftReference {


    private static final Logger log = LoggerFactory.getLogger(EntitySoftReference.class);

    private final DataManager dataManager;
    private final IdSerialization idSerialization;

    public EntitySoftReference(DataManager dataManager, IdSerialization idSerialization) {
        this.dataManager = dataManager;
        this.idSerialization = idSerialization;
    }

    @Nonnull
    public String toEntityReference(@Nullable Object value) {
        return Optional.ofNullable(value)
                .map(it -> idSerialization.idToString(Id.of(it)))
                .orElse("");
    }

    @Nullable
    public Entity toEntity(@Nullable String value) {

        final Id<Object> entityId = entityReferenceOf(value);

        return (Entity) dataManager
                .load(entityId)
                .optional()
                .orElseThrow(() -> new NotExistingEntityReferenceException(value));
    }

    private Id<Object> entityReferenceOf(String value) {
        if (Strings.isNullOrEmpty(value)) {
            log.error("Value is not a valid Entity Soft Reference through IdSerialization: {}", value);
            throw new InvalidEntityReferenceException(value);
        }
        try {
            return idSerialization.stringToId(value);
        } catch (StringIndexOutOfBoundsException e) {
            log.error("Value is not a valid Entity Soft Reference through IdSerialization: {}", value);
            throw new InvalidEntityReferenceException(value, e);
        }
    }
}
