package de.diedavids.jmix.softreference.cuba;

import io.jmix.core.JmixModuleDescriptor;
import io.jmix.core.JmixModules;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JmixModulesDependencyTest {

    @Autowired
    JmixModules jmixModules;

    @Test
    void when_getAll_then_theOrderOfTheDependenciesIsCorrect() {

        assertThat(jmixModules.getAll().stream().map(JmixModuleDescriptor::getId))
				.containsExactly(
                "io.jmix.core",
                "io.jmix.data",
                "io.jmix.eclipselink",
                "io.jmix.ui",
                "de.diedavids.jmix.softreference",
                "de.diedavids.jmix.softreference.cuba",
                "io.jmix.uidata",
                "io.jmix.dynattr",
                "io.jmix.dynattrui",
                "io.jmix.localfs",
                "io.jmix.security",
                "io.jmix.securitydata",
                "io.jmix.securityui",
                "io.jmix.datatools",
                "io.jmix.datatoolsui",
                "io.jmix.audit",
                "io.jmix.auditui",
                "io.jmix.uiexport",
                "io.jmix.email",
                "io.jmix.emailui",
                "com.haulmont.cuba",
                "de.diedavids.jmix.softreference.cuba.test"
        );
    }

    @Test
    void when_getLast_then_SoftReferenceCubaTestConfiguration_shouldAppear() {

        assertThat(jmixModules.getLast().getId())
                .isEqualTo("de.diedavids.jmix.softreference.cuba.test");
    }
}
