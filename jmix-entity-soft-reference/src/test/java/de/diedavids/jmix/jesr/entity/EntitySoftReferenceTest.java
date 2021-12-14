package de.diedavids.jmix.jesr.entity;

import de.diedavids.jmix.jesr.exception.InvalidEntityReferenceException;
import de.diedavids.jmix.jesr.exception.NotExistingEntityReferenceException;
import de.diedavids.jmix.jesr.test_support.Foo;
import de.diedavids.jmix.jesr.test_support.FooProvisioning;
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
            final Foo foo = fooWithRandomId();

            // when
            final String actual = sut.toEntityReference(foo);

            // then
            assertThat(actual).isEqualTo(entityReferenceOf(foo));
        }

        @Test
        void given_nullEntity_expect_emptyString() {

            // when
            final String actual = sut.toEntityReference(null);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class ToEntity {

        @Test
        void given_validEntityRepresentation_expect_validEntity() {

            // given
            Foo foo = fooWithRandomId();

            Foo storedFoo = dataManager.save(foo);

            // when
            final Foo actual = (Foo) sut.toEntity(entityReferenceOf(storedFoo));

            // then
            assertThat(actual.getId()).isEqualTo(storedFoo.getId());
            assertThat(actual.getName()).isEqualTo(storedFoo.getName());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @CsvSource("invalidValue")
        void given_emptyString_expect_NotExistingEntitySoftReferenceException(String invalidValue) {

            // when
            InvalidEntityReferenceException thrown = assertThrows(InvalidEntityReferenceException.class, () ->
                    sut.toEntity(invalidValue)
            );

            // then
            assertThat(thrown.getEntityReference()).isEqualTo(invalidValue);
        }

        @Test
        void given_notExistingSoftReference_expect_InvalidEntitySoftReferenceException() {

            // given
            Foo foo = fooWithRandomId();
            dataManager.save(foo);


            // and
            final UUID notExistingIdOfFoo = UUID.randomUUID();
            Foo notPersistedFoo = fooWithRandomId();
            notPersistedFoo.setId(notExistingIdOfFoo);

            assertThat(dataManager.load(Id.of(notPersistedFoo)).optional())
                    .isEmpty();

            // when
            NotExistingEntityReferenceException thrown = assertThrows(NotExistingEntityReferenceException.class, () ->
                    sut.toEntity(entityReferenceOf(notPersistedFoo))
            );

            // then
            assertThat(thrown.getEntityReference()).isEqualTo(entityReferenceOf(notPersistedFoo));
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