package de.diedavids.jmix.softreference.cuba.entity;

import com.haulmont.cuba.core.global.EntityLoadInfoBuilder;
import de.diedavids.jmix.softreference.cuba.test_support.Document;
import de.diedavids.jmix.softreference.cuba.test_support.DocumentProvisioning;
import io.jmix.core.DataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class EntitySoftReferenceConverterTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    EntityLoadInfoBuilder entityLoadInfoBuilder;

    EntitySoftReferenceConverter sut;

    @BeforeEach
    void setUp() {
        sut = new EntitySoftReferenceConverter();
    }

    @Nested
    class ConvertToDatabaseColumn {

        @Test
        void given_validEntity_expect_validStringRepresentation() {

            // given
            final Document document = documentWithRandomId();

            // when
            final String actual = sut.convertToDatabaseColumn(document);

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
        return entityLoadInfoBuilder.create(storedDocument).toString();
    }

    private Document documentWithRandomId() {
        return DocumentProvisioning.defaultDocumentBuilder()
                .build();
    }

}