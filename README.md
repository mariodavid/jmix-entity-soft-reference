[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) 
![CI Pipeline](https://github.com/mariodavid/jmix-entity-soft-reference/actions/workflows/test.yml/badge.svg)

[![GitHub release](https://img.shields.io/github/release/mariodavid/jmix-entity-soft-reference.svg)](https://github.com/mariodavid/jmix-entity-soft-reference/releases/)
[![Example](https://img.shields.io/badge/example-jmix--entity--soft--reference--example-brightgreen)](https://github.com/mariodavid/jmix-entity-soft-reference-example)
[![Jmix Marketplace](https://img.shields.io/badge/marketplace-jmix--entity--soft--reference-orange)](https://www.jmix.io/marketplace/entity-soft-reference)

# Jmix Soft References for Entities

This addon can be used for referencing entities without a foreign key in the database.

## Installation

Add the dependency to your project:

```groovy
dependencies {
  implementation 'de.diedavids.jmix.softreference:jmix-entity-soft-reference-starter:*addon-version*'
}
```

NOTE: If you are updating from CUBA Platform, see [Migration from CUBA](#migration-from-cuba).

## Concept

### What is a Soft Reference?

A soft reference is a reference in the data model, which does not create a foreign key in the database as well as not creating referential integrity for it. Instead, it is just a "weak" or "soft" reference to another entity.

#### Example: Customer - Order

Let's take the `Customer <>--> Order` example. A customer can have multiple orders. We model that as an composition in CUBA. This means, that there will be a column `CUSTOMER_ID` in the Order table, which stores the primary key of the customer in the Order Table. For every Order, there is a Customer associated to it.

In the soft reference example, it will be different. There will be a `CUSTOMER_ID` column as well in the Order table. But instead there will be no foreign key association on the database. Instead, the following String will be stored in this soft reference column: `example$Customer-2fdc4906-fa89-11e7-8c3f-9a214cf093ae`.


### Foreign keys are valuable, why would someone not want to have them?

Correct. For pretty much every case, there is no need to use soft references. Since it misses all the cool features of referential integrity, there are a lot of downsides. Why do we still need them? Here is an example:

#### Example: Customer - Order - Comment

Let's assume we want to have a column called "Comment". Comments can be created for a lot of different entities. There can be comments for customers as well as comments for orders, products etc. The common way to handle that in a programming language like Java is that you would create an interface called `Commentable`. This interface marks all classes that want to be commentable as such. Then the Comment class could have a reference to a Commentable object. This is what polymorphism is all about.

However - there is no native equivalent in a relational database. The reason is that when you want to create a foreign key, you need to point it to a destination table. But there is no generic table that you could point to. Instead, you want to point to totally different tables.

For cross-cutting functionality, these soft references can be the solution here.


## Example Usage

To see this application component in action, check out this example: [jmix-entity-soft-reference-example](https://github.com/mariodavid/jmix-entity-soft-reference-example).

## Usage

### Entity Attribute

```java
@Entity
class Document {
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
}
```

```java
/**
 * marker interface for entities that support Document references.
 */
public interface SupportsDocumentReference {}
```

### Soft References as Table Columns

In order to use a soft reference as a Table column, there is a class `de.diedavids.jmix.softreference.screen.SoftReferenceInstanceNameTableColumnGenerator` which can be used to create a column which renders the soft reference as a link with the instance name into the table.

Usage:

```
@Subscribe
protected void onInit(InitEvent event) {
    myTable.addGeneratedColumn("softReferenceColumnId",
        new SoftReferenceInstanceNameTableColumnGenerator(
            "softReferenceColumnName",
            uiComponents,
            metadataTools,
            screenBuilders,
            this
        )
    );
}
```

### Soft References as Form fields

In order to render a soft reference into a form, there is a Spring bean `de.diedavids.cuba.entitysoftreference.web.SoftReferenceFormFieldGenerator` which can be utilized.

Usage (in an editor screen):

```
@Inject
private SoftReferenceFormFieldGenerator softReferenceFormFieldGenerator;

@Subscribe
protected void onInit(InitEvent event) {
    softReferenceFormFieldGenerator.initSoftReferenceFormField(form, myFormInstanceContainer, "mySoftReferenceProperty");
}
```


## Migration from CUBA

In case you upgrade from the [CUBA Platform variant of Entity-Soft-Reference](https://github.com/mariodavid/cuba-component-entity-soft-reference), perform the following steps:

* make sure you are using the latest version of CUBA Platform (7.2)
* make sure you are using the latest version of Entity Soft Reference (0.7.0)

* replace `de.diedavids.cuba.entitysoftreference.web.SoftReferenceInstanceNameTableColumnGenerator` --> `de.diedavids.jmix.softreference.screen.SoftReferenceInstanceNameTableColumnGenerator`
* replace `de.diedavids.cuba.entitysoftreference.web.SoftReferenceFormFieldGenerator` --> `de.diedavids.jmix.softreference.screen.SoftReferenceFormFieldGenerator`

### Soft Reference usage

* replace `@MetaProperty(datatype = "EntitySoftReference")` --> `@PropertyDatatype("SoftReference")` when defining a soft reference
* replace `com.haulmont.cuba.core.entity.Entity softReferenceAttribute` with `Object softReferenceAttribute`
* introduce and use marker interface for getter / setter of the `softReferenceAttribute` attribute

#### Example

```java
@Entity
class Document {
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
}
```
