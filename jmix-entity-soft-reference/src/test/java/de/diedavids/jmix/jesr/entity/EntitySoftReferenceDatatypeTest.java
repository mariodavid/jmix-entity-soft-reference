package de.diedavids.jmix.jesr.entity;

import de.diedavids.jmix.jesr.test_support.Foo;
import de.diedavids.jmix.jesr.test_support.FooProvisioning;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.IdSerialization;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class EntitySoftReferenceDatatypeTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    IdSerialization idSerialization;
    @Autowired
    EntitySoftReferenceDatatype sut;


    @Nested
    class Format {

        @Test
        void given_validEntity_expect_validStringRepresentation() {

            // given
            final Foo foo = fooWithRandomId();

            // when
            final String actual = sut.format(foo);

            // then
            assertThat(actual).isEqualTo(entityReferenceOf(foo));
        }
    }

    @Nested
    class Parse {

        @Test
        void given_validEntityRepresentation_expect_validEntity() {

            // given
            Foo foo = fooWithRandomId();

            Foo storedFoo = dataManager.save(foo);

            // when
            final Foo actual = (Foo) sut.parse(entityReferenceOf(storedFoo));

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