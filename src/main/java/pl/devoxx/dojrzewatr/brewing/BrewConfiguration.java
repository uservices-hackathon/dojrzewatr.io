package pl.devoxx.dojrzewatr.brewing;

import com.nurkiewicz.asyncretry.RetryExecutor;
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BrewConfiguration {

    @Bean
    ButelkatrUpdater butelkatrUpdater(ServiceRestClient serviceRestClient, RetryExecutor retryExecutor) {
        return new ButelkatrUpdater(serviceRestClient, retryExecutor, brewProperties());
    }

    @Bean
    BrewProperties brewProperties() {
        return new BrewProperties();
    }
}

