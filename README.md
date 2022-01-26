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

## Differences

Between Jmix and CUBA there are two main differences that are relevant for the entity-soft-reference addon.


### Entity String Representation
The first difference is that the Schema of the String representation of the soft reference has changed. CUBA contained the `com.haulmont.cuba.core.global.EntityLoadInfo` and `com.haulmont.cuba.core.global.EntityLoadInfoBuilder` classes which represents an entity references as `<<MetaClassName>>-<<EntityId>>[-<<ViewName>>]`. Example: `example$Customer-2fdc4906-fa89-11e7-8c3f-9a214cf093ae`.

Jmix does not support this schema anymore. Instead, Jmix offers the `io.jmix.core.IdSerialization` bean, which has the same functionality but the schema looks different. Examples:

```
app_UuidEntity."4e4c5ca2-9a6e-43aa-8e67-3572b674f7c0"
app_LongIdEntity.1234  
app_CompositeKeyEntity.{"entityId":10,"tenant":"abc"}
```

### Entity Interface Support

While in CUBA every entity had to implement the `Entity` interface, Jmix does not have this requirement. The entity interface is added during the enhancement step of the build process. As a consequence it is no longer possible to rely on the common `Entity` interface in this addon and its APIs.

Where in CUBA the soft reference attribute had to be of type `Entity`, for Jmix the attribute needs to be of type `java.lang.Object`.


## CUBA Compatibility Module

In order to make the transition easier when you migrate from CUBA platform, there is a CUBA compatibility module of this addon: `jmix-entity-soft-reference-cuba-starter`. The compatibility module supports the CUBA way of representing entity references as String and also support the `Entity` interface approach. By using this module, there is no need to change something in your application code when transitioning to Jmix.

### Soft Reference usage

* replace `@MetaProperty(datatype = "EntitySoftReference")` --> `@PropertyDatatype("EntitySoftReference")` when defining a soft reference
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


### Change Imports for UI helper classes
* replace `de.diedavids.cuba.entitysoftreference.web.SoftReferenceInstanceNameTableColumnGenerator` --> `de.diedavids.jmix.softreference.screen.SoftReferenceInstanceNameTableColumnGenerator`
* replace `de.diedavids.cuba.entitysoftreference.web.SoftReferenceFormFieldGenerator` --> `de.diedavids.jmix.softreference.screen.SoftReferenceFormFieldGenerator`

### Example application: CUBA migration

You can find an example application, that has been initially developed with CUBA as a migrated project to a Jmix application. It uses the CUBA compatibility module and also the soft-reference CUBA compatibility module:

* original application: https://github.com/mariodavid/cuba-example-using-entity-soft-reference
* migrated application: https://github.com/mariodavid/jmix-entity-soft-reference-cuba-example


## Migrate away fom CUBA Compatability Module

After you have done the initial migration of your CUBA platform project to Jmix and are running now a Jmix project with the CUBA compatibility module and the entity soft reference CUBA compatibility module, you might want to refactor your Jmix application at some point to get rid of the CUBA compatibility and instead have a legacy-free Jmix application.

In order to achieve this goal, you have to perform the following steps for this addon:

### Data Migration

The most important step is to migrate the data. The data of all your soft references attributes has to change from the CUBA entity representation to the Jmix entity representation. Let's look into an example. Assuming you have a `Document` entity with a soft reference attribute `refersTo`. It is possible to reference either `Customer` or `Order` entities. In this case your DB table might look like this:

```sql
SELECT id, name, refers_to FROM ceuesr_document;
```

Result:

| id | name | refers\_to |
| :--- | :--- | :--- |
| 9c2f8337-0f41-8980-ae83-86c861dc8d29 | Customer 2 Document | ceuesr\_Customer-01c4d333-7f24-8da9-9416-815e57469a0d |
| 6d89abbb-777f-78d8-c82b-cf536c1192c5 | Order 1 Document | ceuesr\_Order-50edc3a4-794a-adf4-d802-592e74674098 |
| 5d6fe3ca-f8f9-9af5-eb47-c1151178ca2e | Customer 1 Document | ceuesr\_Customer-05441f6d-fbb5-284d-5475-faa32265cc20 |

So there are difference references to different entities (like Customer and Order in this example).

To support you with this DB migration effort, the entity soft reference CUBA compatibility module contains a Service that automates this process: `de.diedavids.jmix.softreference.cuba.SoftReferenceMigrationService`.

The service contains the following methods:

```java
/**
 * Services that performs data migration from the CUBA entity representation to the Jmix entity representation.
 *
 * CUBA representation: example$Customer-2fdc4906-fa89-11e7-8c3f-9a214cf093ae (@see {@link com.haulmont.cuba.core.global.EntityLoadInfo}
 * Jmix representation: example$Customer."2fdc4906-fa89-11e7-8c3f-9a214cf093ae" (@see {@link io.jmix.core.IdSerialization}).
 *
 */
public interface SoftReferenceMigrationService {
    String NAME = "softreference_SoftReferenceMigrationService";

    /**
     * migrates all soft references for a given attribute form CUBA to Jmix representation
     *
     * @param <T> type of the entity that holds the soft references
     * @param entityClass the entity type that contains the soft reference attribute to migrate
     * @param propertyWithCubaFormat the property of the entity class that contains the CUBA soft references
     * @param propertyWithJmixFormat the new property of the entity class that shall contain the Jmix soft reference format
     * @param batchSize the size of batch that is executed per transaction.
     * @return true, if all soft references have been migrated, otherwise false
     */
    <T extends Entity> int migrateSoftReferenceAttribute(
            Class<T> entityClass,
            String propertyWithCubaFormat,
            String propertyWithJmixFormat,
            int batchSize
    );

    /**
     * migrates all soft references for a given attribute form CUBA to Jmix representation in a batched form
     *
     * @param <T> type of the entity that holds the soft references
     * @param entityClass the entity type that contains the soft reference attribute to migrate
     * @param propertyWithCubaFormat the property of the entity class that contains the CUBA soft references
     * @param propertyWithJmixFormat the new property of the entity class that shall contain the Jmix soft reference format
     * @param batchSize the size of batch that is executed per transaction.
     * @param sortProperty the attribute to sort by when batching is performed
     * @return true, if all soft references have been migrated, otherwise false
     */
    <T extends Entity> int migrateSoftReferenceAttribute(
            Class<T> entityClass,
            String propertyWithCubaFormat,
            String propertyWithJmixFormat,
            int batchSize,
            String sortProperty
    );
}
```

One of those methods can be used programmatically to trigger the migration. Those methods will simply copy & reformat the values from the CUBA format to the Jmix format. NOTE: it will not touch the old values at all.

INFO: The migration is idempotent, so it is safe to perform the same migration multiple times.

After this migration happened, you need to make sure in your usage of the addon, that the application does not continue to write into the fields. Depending on how much time is between the migration and the deployment of the new source code that only interacts with the new Jmix format, it might be required to perform an additional data migration after the deployment.
