package de.diedavids.jmix.softreference.cuba;

import de.diedavids.jmix.softreference.SoftReferenceConfiguration;
import io.jmix.core.annotation.JmixModule;
import io.jmix.core.impl.scanning.AnnotationScanMetadataReaderFactory;
import io.jmix.eclipselink.EclipselinkConfiguration;
import io.jmix.ui.UiConfiguration;
import io.jmix.ui.sys.UiControllersConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Collections;

@Configuration
@ComponentScan
@ConfigurationPropertiesScan
@JmixModule(dependsOn = {
        EclipselinkConfiguration.class,
        UiConfiguration.class,
        SoftReferenceConfiguration.class
})
@PropertySource(name = "de.diedavids.jmix.softreference.cuba", value = "classpath:/de/diedavids/jmix/softreference/cuba/module.properties")
public class SoftReferenceCubaConfiguration {

    @Bean("softreference_SoftReferenceUiControllers")
    public UiControllersConfiguration screens(ApplicationContext applicationContext,
                                              AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        UiControllersConfiguration uiControllers
                = new UiControllersConfiguration(applicationContext, metadataReaderFactory);
        uiControllers.setBasePackages(Collections.singletonList("de.diedavids.jmix.softreference.cuba"));
        return uiControllers;
    }
}
