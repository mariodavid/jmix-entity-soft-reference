package de.diedavids.jmix.softreference.test_support;

import de.diedavids.jmix.softreference.entity.SoftReferenceConverter;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.PropertyDatatype;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Entity(name = "softreference_Document")
@Table(name = "SOFTREFERENCE_DOCUMENT")
public class Document {


    @Id
    @Column(name = "ID", nullable = false)
    @JmixGeneratedValue
    private UUID id;

    @Composition
    @OneToMany(mappedBy = "document")
    private List<Tag> tags;

    @InstanceName
    @Column(name = "NAME", nullable = false)
    protected String name;

    @PropertyDatatype("SoftReference")
    @Column(name = "REFERS_TO")
    @Convert(converter = SoftReferenceConverter.class)
    private Object refersTo;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setRefersTo(Object refersTo) {
        this.refersTo = refersTo;
    }

    public Object getRefersTo() {
        return refersTo;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}