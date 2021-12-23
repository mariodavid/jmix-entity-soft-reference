package de.diedavids.jmix.jesr.entity;

import de.diedavids.jmix.jesr.exception.InvalidEntityReferenceException;
import de.diedavids.jmix.jesr.exception.NotExistingEntityReferenceException;
import de.diedavids.jmix.jesr.test_support.Document;
import de.diedavids.jmix.jesr.test_support.DocumentProvisioning;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.IdSerialization;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class EntitySoftReferenceTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    IdSerialization idSerialization;
    @Autowired
    EntitySoftReference sut;

    @Nested
    class ToEntityReference {

        @Test
        void given_validEntity_expect_validStringRepresentation() {

            // given
            final Document document = fooWithRandomId();

            // when
            final String actual = sut.toEntityReference(document);

            // then
            assertThat(actual).isEqualTo(entityReferenceOf(document));
        }

        @Test
        void given_nullEntity_expect_emptyString() {

            // when
            final String actual = sut.toEntityReference(null);

            // then
            assertThat(actual).isNull();
        }
    }

    @Nested
    class ToEntity {

        @Test
        void given_validEntityRepresentation_expect_validEntity() {

            // given
            Document document = fooWithRandomId();

            Document storedDocument = dataManager.save(document);

            // when
            final Document actual = (Document) sut.toEntity(entityReferenceOf(storedDocument));

            // then
            assertThat(actual.getId()).isEqualTo(storedDocument.getId());
            assertThat(actual.getName()).isEqualTo(storedDocument.getName());
        }

        @Test
        void given_emptyString_expect_NotExistingEntitySoftReferenceException() {

            // when
            InvalidEntityReferenceException thrown = assertThrows(InvalidEntityReferenceException.class, () ->
                    sut.toEntity("invalidValue")
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
            NotExistingEntityReferenceException thrown = assertThrows(NotExistingEntityReferenceException.class, () ->
                    sut.toEntity(entityReferenceOf(notPersistedDocument))
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