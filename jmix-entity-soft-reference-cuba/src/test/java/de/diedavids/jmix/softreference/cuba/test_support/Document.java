package de.diedavids.jmix.softreference.cuba.test_support;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import de.diedavids.jmix.softreference.cuba.entity.EntitySoftReferenceConverter;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}