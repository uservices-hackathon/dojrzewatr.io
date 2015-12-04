package pl.uservices.dojrzewatr.brewing;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.sleuth.TraceManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class BrewConfiguration {

    @Bean
    BottlingServiceUpdater butelkatrUpdater(TraceManager trace, PresentingServiceClient prezentatrClient,
                                            BottlingServiceClient bottlingServiceClient,
                                            @LoadBalanced RestTemplate restTemplate) {
        return new BottlingServiceUpdater(brewProperties(), trace, prezentatrClient,
                bottlingServiceClient, restTemplate);
    }

    @Bean
    BrewProperties brewProperties() {
        return new BrewProperties();
    }

}

