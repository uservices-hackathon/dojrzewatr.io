package pl.devoxx.dojrzewatr.brewing;

import com.nurkiewicz.asyncretry.RetryExecutor;
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient;
import lombok.extern.slf4j.Slf4j;
import pl.devoxx.dojrzewatr.brewing.model.Version;

import static com.netflix.hystrix.HystrixCommand.Setter.withGroupKey;
import static com.netflix.hystrix.HystrixCommandGroupKey.Factory.asKey;

@Slf4j
class ButelkatrUpdater {

    private final ServiceRestClient serviceRestClient;
    private final RetryExecutor retryExecutor;
    private final BrewProperties brewProperties;

    public ButelkatrUpdater(ServiceRestClient serviceRestClient, RetryExecutor retryExecutor, BrewProperties brewProperties) {
        this.serviceRestClient = serviceRestClient;
        this.retryExecutor = retryExecutor;
        this.brewProperties = brewProperties;
    }

    void updateButelkatrAboutBrewedBeer() {
        try {
            Long timeout = brewProperties.getTimeout();
            log.info("Brewing beer... it will take [{}] ms", timeout);
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            log.error("Exception occurred while brewing beer", e);
        }
        notifyButelkatr();
    }

    private void notifyButelkatr() {
        serviceRestClient.forService("butelkatr")
                .retryUsing(retryExecutor)
                .post()
                .withCircuitBreaker(withGroupKey(asKey("butelkatr_notification")))
                .onUrl("/bottle")
                .withoutBody()
                .withHeaders().contentType(Version.BUTELKATR_V1)
                .andExecuteFor()
                .ignoringResponseAsync();
    }

}
