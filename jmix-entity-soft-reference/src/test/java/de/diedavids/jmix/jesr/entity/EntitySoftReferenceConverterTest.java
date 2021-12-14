package de.diedavids.jmix.jesr.entity;

import de.diedavids.jmix.jesr.test_support.Foo;
import de.diedavids.jmix.jesr.test_support.FooProvisioning;
import io.jmix.core.DataManager;
import io.jmix.core.Entity;
import io.jmix.core.Id;
import io.jmix.core.IdSerialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class EntitySoftReferenceConverterTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    IdSerialization idSerialization;
    @Autowired
    EntitySoftReference entitySoftReference;

    EntitySoftReferenceConverter sut;

    @BeforeEach
    void setUp() {
        sut = new EntitySoftReferenceConverter(
                entitySoftReference
        );
    }

    @Nested
    class ConvertToDatabaseColumn {

        @Test
        void given_validEntity_expect_validStringRepresentation() {

            // given
            final Foo foo = fooWithRandomId();

            // when
            final String actual = sut.convertToDatabaseColumn((Entity) foo);

            // then
            assertThat(actual).isEqualTo(entityReferenceOf(foo));
        }

    }

    @Nested
    class ConvertToEntityAttribute {

        @Test
        void given_validEntityRepresentation_expect_validEntity() {

            // given
            Foo foo = fooWithRandomId();

            Foo storedFoo = dataManager.save(foo);

            // when
            final Foo actual = (Foo) sut.convertToEntityAttribute(entityReferenceOf(storedFoo));

            // then
            assertThat(actual.getId()).isEqualTo(storedFoo.getId());
            assertThat(actual.getName()).isEqualTo(storedFoo.getName());
        }
    }

    private String entityReferenceOf(Foo storedFoo) {
        return idSerialization.idToString(Id.of(storedFoo));
    }

    private Foo fooWithRandomId() {
        return FooProvisioning.defaultFooBuilder()
                .build();
    }

}