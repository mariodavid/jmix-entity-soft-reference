package de.diedavids.jmix.softreference.cuba.entity;

import com.haulmont.cuba.core.global.EntityLoadInfoBuilder;
import de.diedavids.jmix.softreference.cuba.test_support.Document;
import de.diedavids.jmix.softreference.cuba.test_support.DocumentProvisioning;
import io.jmix.core.DataManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class EntitySoftReferenceDatatypeTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    EntityLoadInfoBuilder entityLoadInfoBuilder;
    @Autowired
    EntitySoftReferenceDatatype sut;


    @Nested
    class Format {

        @Test
        void given_validEntity_expect_validStringRepresentation() {

            // given
            final Document document = fooWithRandomId();

            // when
            final String actual = sut.format(document);

            // then
            assertThat(actual).isEqualTo(entityReferenceOf(document));
        }
    }

    @Nested
    class Parse {

        @Test
        void given_validEntityRepresentation_expect_validEntity() throws ParseException {

            // given
            Document document = fooWithRandomId();

            Document storedDocument = dataManager.save(document);

            // when
            final Document actual = (Document) sut.parse(entityReferenceOf(storedDocument));

            // then
            assertThat(actual.getId()).isEqualTo(storedDocument.getId());
            assertThat(actual.getName()).isEqualTo(storedDocument.getName());
        }
    }

    private String entityReferenceOf(Document storedDocument) {
        return entityLoadInfoBuilder.create(storedDocument).toString();
    }

    private Document fooWithRandomId() {
        return DocumentProvisioning.defaultDocumentBuilder()
                .build();
    }

}