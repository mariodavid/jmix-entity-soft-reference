package de.diedavids.jmix.softreference.cuba.test_support;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@JmixEntity
@Table(name = "CUSTOMER")
@Entity
public class Customer extends BaseUuidEntity implements SupportsDocumentReference {

    @InstanceName
    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}