package de.diedavids.jmix.softreference.cuba.impl;

import de.diedavids.jmix.softreference.cuba.SoftReferenceMigrationService;
import io.jmix.core.Entity;
import org.springframework.stereotype.Component;

@Component(SoftReferenceMigrationService.NAME)
public class SoftReferenceMigrationServiceBean implements SoftReferenceMigrationService {

    @Override
    public <T extends Entity> boolean migrateSoftReferenceAttribute(Class<T> entityClass, String attribute, String newAttribute) {
        return false;
    }
}
