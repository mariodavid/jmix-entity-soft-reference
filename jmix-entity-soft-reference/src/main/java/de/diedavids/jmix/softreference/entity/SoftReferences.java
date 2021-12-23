package de.diedavids.jmix.softreference.entity;

import com.google.common.base.Strings;
import de.diedavids.jmix.softreference.exception.InvalidSoftReferenceException;
import de.diedavids.jmix.softreference.exception.NotExistingSoftReferenceException;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.IdSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Nullable;


@Component
public class SoftReferences {

    private static final Logger log = LoggerFactory.getLogger(SoftReferences.class);

    private final DataManager dataManager;
    private final IdSerialization idSerialization;

    public SoftReferences(DataManager dataManager, IdSerialization idSerialization) {
        this.dataManager = dataManager;
        this.idSerialization = idSerialization;
    }

    public <E> String softReferenceOf(@Nullable E value) {

        if (value == null) {
            return null;
        }

        return idSerialization.idToString(Id.of(value));
    }

    @Nullable
    public Object entityOf(@Nullable String value) {

        if (Strings.isNullOrEmpty(value)) {
            return null;
        }

        final Id<Object> entityId = entityReferenceOf(value);

        return dataManager
                .load(entityId)
                .optional()
                .orElseThrow(() -> new NotExistingSoftReferenceException(value));
    }

    private Id<Object> entityReferenceOf(String value) {
        try {
            return idSerialization.stringToId(value);
        } catch (StringIndexOutOfBoundsException e) {
            log.error("Value is not a valid Entity Soft Reference through IdSerialization: {}", value);
            throw new InvalidSoftReferenceException(value, e);
        }
    }
}
