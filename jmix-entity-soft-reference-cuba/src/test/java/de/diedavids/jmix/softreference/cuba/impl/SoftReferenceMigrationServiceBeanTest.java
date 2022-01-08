package de.diedavids.jmix.softreference.cuba.impl;

import de.diedavids.jmix.softreference.cuba.SoftReferenceMigrationService;
import de.diedavids.jmix.softreference.cuba.test_support.Customer;
import de.diedavids.jmix.softreference.cuba.test_support.Document;
import de.diedavids.jmix.softreference.cuba.test_support.DocumentProvisioning;
import de.diedavids.jmix.softreference.cuba.test_support.SupportsDocumentReference;
import de.diedavids.jmix.softreference.cuba.test_support.Tag;
import io.jmix.core.DataManager;
import io.jmix.core.EntityStates;
import io.jmix.core.Id;
import io.jmix.core.SaveContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SoftReferenceMigrationServiceBeanTest {


    @Autowired
    DataManager dataManager;
    @Autowired
    EntityStates entityStates;
    @Autowired
    SoftReferenceMigrationService sut;


    private Customer customer1;
    private Customer customer2;


    @BeforeEach
    void setUp() {
        customer1 = storeCustomer("Customer 1");
        customer2 = storeCustomer("Customer 2");
    }


    @Nested
    class MigrateSoftReferenceAttribute {

        @Test
        void given_validCUBASoftReference_when_migration_then_validJmixSoftReference() {

            // given
            final Document document = storeDocumentWithReference(customer1);

            // when
            sut.migrateSoftReferenceAttribute(
                    Document.class,
                    "refersTo",
                    "refersToJmix"
            );

            // and
            final Document reloadedDocument = dataManager.load(Id.of(document)).one();

            // then
            final SupportsDocumentReference newCustomerReference = reloadedDocument.getRefersToJmix();

            assertThat(newCustomerReference)
                    .isEqualTo(customer1);



        }

    }



    private Document documentWithRandomId() {
        return DocumentProvisioning.defaultDocumentBuilder()
                .build();
    }


    private Document storeDocumentWithReference(Customer customer) {
        final Document document = documentWithRandomId();
        document.setName("Document for " + customer.getName());
        document.setRefersTo(customer);

        return dataManager.save(document);
    }

    private Document storeDocumentWithReferenceAndTags(Customer customer, String... tagNames) {

        final Document document = documentWithRandomId();
        document.setName("Document for " + customer.getName());
        document.setRefersTo(customer);

        final SaveContext saveContext = new SaveContext();
        saveContext.saving(document);

        Arrays.stream(tagNames)
                .map(tagName -> createTagFor(document, tagName))
                .forEach(saveContext::saving);

        dataManager.save(saveContext);

        return dataManager.load(Id.of(document))
                .one();
    }

    private Tag createTagFor(Document document, String tagName) {
        final Tag tag = dataManager.create(Tag.class);
        tag.setName(tagName);
        tag.setDocument(document);
        return tag;
    }

    private Customer storeCustomer(String name) {
        Customer customer = dataManager.create(Customer.class);
        customer.setName(name);
        return dataManager.save(customer);
    }
}