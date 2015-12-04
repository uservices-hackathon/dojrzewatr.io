package pl.uservices.dojrzewatr.brewing;

import org.springframework.cloud.sleuth.TraceManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BrewConfiguration {

    @Bean
    ButelkatrUpdater butelkatrUpdater(TraceManager trace, PrezentatrClient prezentatrClient,
                                      ButelkatrClient butelkatrClient) {
        return new ButelkatrUpdater(brewProperties(), trace, prezentatrClient, butelkatrClient);
    }

    @Bean
    BrewProperties brewProperties() {
        return new BrewProperties();
    }

}

