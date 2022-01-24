package de.diedavids.jmix.softreference.cuba.impl;

import de.diedavids.jmix.softreference.cuba.SoftReferenceMigrationService;
import de.diedavids.jmix.softreference.cuba.test_support.Customer;
import de.diedavids.jmix.softreference.cuba.test_support.Document;
import de.diedavids.jmix.softreference.cuba.test_support.DocumentProvisioning;
import de.diedavids.jmix.softreference.cuba.test_support.SupportsDocumentReference;
import io.jmix.core.DataManager;
import io.jmix.core.EntityStates;
import io.jmix.core.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SoftReferenceMigrationServiceBeanTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataManager dataManager;
    @Autowired
    EntityStates entityStates;
    @Autowired
    SoftReferenceMigrationService sut;

    private Customer customer;
    private Document document;


    @BeforeEach
    void setUp() {
        customer = storeCustomer("Customer 1");
        document = storeDocumentWithReference(customer);

    }


    @Nested
    class MigrateSoftReferenceAttribute {

        @Test
        void given_validCUBASoftReference_when_migration_then_validJmixSoftReference() {

            // given
            final Document document = storeDocumentWithReference(customer);

            // when
            sut.migrateSoftReferenceAttribute(
                    Document.class,
                    "refersTo",
                    "refersToJmix",
                    "id"
            );

            // and
            final Document reloadedDocument = dataManager.load(Id.of(document)).one();

            // then
            final SupportsDocumentReference newCustomerReference = reloadedDocument.getRefersToJmix();

            assertThat(newCustomerReference)
                    .isEqualTo(customer);
        }

        @Test
        void given_noSortPropertyIsProvided_and_noCreatedDateAttributeInEntityClass_when_migrate_then_IllegalStateException() {

            // given
            storeDocumentWithReference(customer);

            // when: not passing in a sort property

            assertThatThrownBy(() ->
                    sut.migrateSoftReferenceAttribute(
                    Document.class, // but Document does not have any property annotated with @CreatedDate
                    "refersTo",
                    "refersToJmix"
            ))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No Entity property present with annotation @CreatedDate to sort by. In case you don't have a creation timestamp property in your entity, you need to pass in the property to sort by during migration");
        }

        @Test
        void given_validCUBASoftReferenceInDatabase_when_migration_then_validJmixSoftReferenceInDatabase() {

            // when
            sut.migrateSoftReferenceAttribute(
                    Document.class,
                    "refersTo",
                    "refersToJmix",
                    "id"
            );

            // and
            final Map<String, Object> rawDocument = jdbcTemplate.queryForList("select * from SOFTREFERENCE_DOCUMENT where ID = '" + document.getId().toString() + "'").get(0);

            assertThat(rawDocument.get("ID"))
                    .isEqualTo(document.getId());

            assertThat(rawDocument.get("REFERS_TO_JMIX"))
                    .isEqualTo(String.format("Customer.\"%s\"", customer.getId().toString()));

            assertThat(rawDocument.get("REFERS_TO"))
                    .isEqualTo(String.format("Customer-%s", customer.getId().toString()));
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

    private Customer storeCustomer(String name) {
        Customer customer = dataManager.create(Customer.class);
        customer.setName(name);
        return dataManager.save(customer);
    }
}