package de.diedavids.jmix.softreference.cuba;

import com.haulmont.cuba.CubaConfiguration;
import de.diedavids.jmix.softreference.SoftReferenceConfiguration;
import io.jmix.core.annotation.JmixModule;
import io.jmix.core.security.InMemoryUserRepository;
import io.jmix.core.security.UserRepository;
import io.jmix.eclipselink.EclipselinkConfiguration;
import io.jmix.ui.UiConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import({
        SoftReferenceConfiguration.class,
        SoftReferenceCubaConfiguration.class
})
@PropertySource("classpath:/test_support/cuba-application.properties")
@JmixModule(
    id = "de.diedavids.jmix.softreference.cuba.test",
    dependsOn = {
        EclipselinkConfiguration.class,
        UiConfiguration.class,
        SoftReferenceConfiguration.class,
        SoftReferenceCubaConfiguration.class,
        CubaConfiguration.class,
})
public class SoftReferenceCubaTestConfiguration {


    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    
    @Bean
    @Primary
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }
}
