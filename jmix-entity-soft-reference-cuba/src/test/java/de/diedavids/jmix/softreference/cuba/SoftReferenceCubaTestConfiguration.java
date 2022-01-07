package de.diedavids.jmix.softreference.cuba;

import io.jmix.core.annotation.JmixModule;
import io.jmix.core.security.InMemoryUserRepository;
import io.jmix.core.security.UserRepository;
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
@Import(SoftReferenceCubaConfiguration.class)
@PropertySource("classpath:/test_support/cuba-application.properties")
@JmixModule(dependsOn = SoftReferenceCubaConfiguration.class)
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
