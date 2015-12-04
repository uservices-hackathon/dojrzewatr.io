package pl.uservices.dojrzewatr.brewing;

import org.springframework.cloud.sleuth.TraceManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BrewConfiguration {

    @Bean
    BottlingServiceUpdater butelkatrUpdater(TraceManager trace, PresentingServiceClient prezentatrClient,
                                            BottlingServiceClient bottlingServiceClient) {
        return new BottlingServiceUpdater(brewProperties(), trace, prezentatrClient, bottlingServiceClient);
    }

    @Bean
    BrewProperties brewProperties() {
        return new BrewProperties();
    }

}

