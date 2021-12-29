package de.diedavids.jmix.softreference.test_support;

import de.diedavids.jmix.softreference.entity.SoftReferenceConverter;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.PropertyDatatype;

import javax.persistence.*;
import java.util.UUID;

@JmixEntity
@Entity(name = "softreference_Document")
@Table(name = "SOFTREFERENCE_DOCUMENT")
public class Document {


    @Id
    @Column(name = "ID")
    @JmixGeneratedValue
    private UUID id;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @InstanceName
    @Column(name = "NAME", nullable = false)
    protected String name;

    @PropertyDatatype("SoftReference")
    @Column(name = "REFERS_TO")
    @Convert(converter = SoftReferenceConverter.class)
    private Object refersTo;

    public SupportsDocumentReference getRefersTo() {
        return (SupportsDocumentReference) refersTo;
    }

    public void setRefersTo(SupportsDocumentReference refersTo) {
        this.refersTo = refersTo;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}