package de.diedavids.jmix.softreference.entity;

import de.diedavids.jmix.softreference.exception.InvalidSoftReferenceException;
import de.diedavids.jmix.softreference.exception.NotExistingSoftReferenceException;
import de.diedavids.jmix.softreference.test_support.Document;
import de.diedavids.jmix.softreference.test_support.DocumentProvisioning;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.IdSerialization;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class SoftReferencesTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    IdSerialization idSerialization;
    @Autowired
    SoftReferences sut;

    @Nested
    class SoftReferenceOf {

        @Test
        void given_validEntity_expect_validStringRepresentation() {

            // given
            final Document document = fooWithRandomId();

            // when
            final String actual = sut.softReferenceOf(document);

            // then
            assertThat(actual).isEqualTo(entityReferenceOf(document));
        }

        @Test
        void given_nullEntity_expect_emptyString() {

            // when
            final String actual = sut.softReferenceOf(null);

            // then
            assertThat(actual).isNull();
        }
    }

    @Nested
    class EntityOf {

        @Test
        void given_validEntityRepresentation_expect_validEntity() {

            // given
            Document document = fooWithRandomId();

            Document storedDocument = dataManager.save(document);

            // when
            final Document actual = (Document) sut.entityOf(entityReferenceOf(storedDocument));

            // then
            assertThat(actual.getId()).isEqualTo(storedDocument.getId());
            assertThat(actual.getName()).isEqualTo(storedDocument.getName());
        }

        @Test
        void given_emptyString_expect_NotExistingEntitySoftReferenceException() {

            // when
            InvalidSoftReferenceException thrown = assertThrows(InvalidSoftReferenceException.class, () ->
                    sut.entityOf("invalidValue")
            );

            // then
            assertThat(thrown.getEntityReference()).isEqualTo("invalidValue");
        }

        @Test
        void given_notExistingSoftReference_expect_InvalidEntitySoftReferenceException() {

            // given
            Document document = fooWithRandomId();
            dataManager.save(document);


            // and
            final UUID notExistingIdOfFoo = UUID.randomUUID();
            Document notPersistedDocument = fooWithRandomId();
            notPersistedDocument.setId(notExistingIdOfFoo);

            assertThat(dataManager.load(Id.of(notPersistedDocument)).optional())
                    .isEmpty();

            // when
            NotExistingSoftReferenceException thrown = assertThrows(NotExistingSoftReferenceException.class, () ->
                    sut.entityOf(entityReferenceOf(notPersistedDocument))
            );

            // then
            assertThat(thrown.getEntityReference()).isEqualTo(entityReferenceOf(notPersistedDocument));
        }
    }

    private String entityReferenceOf(Document storedDocument) {
        return idSerialization.idToString(Id.of(storedDocument));
    }

    private Document fooWithRandomId() {
        return DocumentProvisioning.defaultFooBuilder()
                .build();
    }

}