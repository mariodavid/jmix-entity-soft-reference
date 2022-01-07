package de.diedavids.jmix.softreference.cuba.test_support;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@JmixEntity
@Table(name = "SOFTREFERENCE_TAG")
@Entity(name = "softreference_Tag")
public class Tag extends BaseUuidEntity {

    @InstanceName
    @Column(name = "NAME")
    private String name;
    
    @JoinColumn(name = "DOCUMENT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Document document;

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}