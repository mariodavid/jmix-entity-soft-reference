package de.diedavids.jmix.jesr.entity;

import com.google.common.base.Strings;
import de.diedavids.jmix.jesr.exception.InvalidEntityReferenceException;
import de.diedavids.jmix.jesr.exception.NotExistingEntityReferenceException;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.IdSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Nullable;


@Component
public class EntitySoftReference {

    private static final Logger log = LoggerFactory.getLogger(EntitySoftReference.class);

    private final DataManager dataManager;
    private final IdSerialization idSerialization;

    public EntitySoftReference(DataManager dataManager, IdSerialization idSerialization) {
        this.dataManager = dataManager;
        this.idSerialization = idSerialization;
    }

    public <E> String toEntityReference(@Nullable E value) {

        if (value == null) {
            return null;
        }

        return idSerialization.idToString(Id.of(value));
    }

    @Nullable
    public Object toEntity(@Nullable String value) {

        if (Strings.isNullOrEmpty(value)) {
            return null;
        }

        final Id<Object> entityId = entityReferenceOf(value);

        return dataManager
                .load(entityId)
                .optional()
                .orElseThrow(() -> new NotExistingEntityReferenceException(value));
    }

    private Id<Object> entityReferenceOf(String value) {
        try {
            return idSerialization.stringToId(value);
        } catch (StringIndexOutOfBoundsException e) {
            log.error("Value is not a valid Entity Soft Reference through IdSerialization: {}", value);
            throw new InvalidEntityReferenceException(value, e);
        }
    }
}
