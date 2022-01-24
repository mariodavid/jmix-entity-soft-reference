package de.diedavids.jmix.softreference.cuba.test_support;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import de.diedavids.jmix.softreference.cuba.entity.EntitySoftReferenceConverter;
import de.diedavids.jmix.softreference.entity.SoftReferenceConverter;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.PropertyDatatype;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@JmixEntity
@Entity(name = "softreference_Document")
@Table(name = "SOFTREFERENCE_DOCUMENT")
public class Document extends BaseUuidEntity {

    @Composition
    @OneToMany(mappedBy = "document")
    private List<Tag> tags;

    @InstanceName
    @Column(name = "NAME", nullable = false)
    protected String name;

    @PropertyDatatype("EntitySoftReference")
    @Column(name = "REFERS_TO")
    @Convert(converter = EntitySoftReferenceConverter.class)
    private Object refersTo;


    @PropertyDatatype("SoftReference")
    @Column(name = "REFERS_TO_JMIX")
    @Convert(converter = SoftReferenceConverter.class)
    private Object refersToJmix;


    public SupportsDocumentReference getRefersToJmix() {
        return (SupportsDocumentReference) refersToJmix;
    }

    public void setRefersToJmix(SupportsDocumentReference refersToJmix) {
        this.refersToJmix = refersToJmix;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setRefersTo(io.jmix.core.Entity refersTo) {
        this.refersTo = refersTo;
    }

    public io.jmix.core.Entity getRefersTo() {
        return (io.jmix.core.Entity) refersTo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}