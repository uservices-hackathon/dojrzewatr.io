package pl.devoxx.dojrzewatr.brewing;

import com.codahale.metrics.MetricRegistry;
import com.nurkiewicz.asyncretry.RetryExecutor;
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BrewConfiguration {

    @Bean
    ButelkatrUpdater butelkatrUpdater(ServiceRestClient serviceRestClient, RetryExecutor retryExecutor, MetricRegistry metricRegistry) {
        return new ButelkatrUpdater(serviceRestClient, retryExecutor, brewProperties(), metricRegistry);
    }

    @Bean
    BrewProperties brewProperties() {
        return new BrewProperties();
    }
}

