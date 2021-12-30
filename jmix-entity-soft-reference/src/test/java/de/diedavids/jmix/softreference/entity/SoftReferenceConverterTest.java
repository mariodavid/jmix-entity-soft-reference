package de.diedavids.jmix.softreference.entity;

import de.diedavids.jmix.softreference.test_support.Document;
import de.diedavids.jmix.softreference.test_support.DocumentProvisioning;
import io.jmix.core.DataManager;
import io.jmix.core.Entity;
import io.jmix.core.Id;
import io.jmix.core.IdSerialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class SoftReferenceConverterTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    IdSerialization idSerialization;
    @Autowired
    SoftReferences softReferences;

    SoftReferenceConverter sut;

    @BeforeEach
    void setUp() {
        sut = new SoftReferenceConverter();
    }

    @Nested
    class ConvertToDatabaseColumn {

        @Test
        void given_validEntity_expect_validStringRepresentation() {

            // given
            final Document document = documentWithRandomId();

            // when
            final String actual = sut.convertToDatabaseColumn((Entity) document);

            // then
            assertThat(actual).isEqualTo(entityReferenceOf(document));
        }

    }

    @Nested
    class ConvertToEntityAttribute {

        @Test
        void given_validEntityRepresentation_expect_validEntity() {

            // given
            Document document = documentWithRandomId();

            Document storedDocument = dataManager.save(document);

            // when
            final Document actual = (Document) sut.convertToEntityAttribute(entityReferenceOf(storedDocument));

            // then
            assertThat(actual.getId()).isEqualTo(storedDocument.getId());
            assertThat(actual.getName()).isEqualTo(storedDocument.getName());
        }
    }

    private String entityReferenceOf(Document storedDocument) {
        return idSerialization.idToString(Id.of(storedDocument));
    }

    private Document documentWithRandomId() {
        return DocumentProvisioning.defaultDocumentBuilder()
                .build();
    }

}