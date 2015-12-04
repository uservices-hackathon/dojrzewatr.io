package pl.uservices.dojrzewatr.brewing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.TraceManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import pl.uservices.dojrzewatr.brewing.model.Ingredients;
import pl.uservices.dojrzewatr.brewing.model.Wort;

@Slf4j
class BottlingServiceUpdater {

    private final BrewProperties brewProperties;
    private final TraceManager traceManager;
    private final PresentingServiceClient prezentatrClient;
    private final BottlingServiceClient bottlingServiceClient;

    public BottlingServiceUpdater(BrewProperties brewProperties,
                                  TraceManager traceManager,
                                  PresentingServiceClient prezentatrClient,
                                  BottlingServiceClient bottlingServiceClient) {
        this.brewProperties = brewProperties;
        this.traceManager = traceManager;
        this.prezentatrClient = prezentatrClient;
        this.bottlingServiceClient = bottlingServiceClient;
    }

    @Async
    public void updateBottlingServiceAboutBrewedBeer(final Ingredients ingredients, String processId) {
        log.info("Current trace id is equal [{}]", processId);
        notifyPresentingService(processId);
        try {
            Long timeout = brewProperties.getTimeout();
            log.info("Brewing beer... it will take [{}] ms", timeout);
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            log.error("Exception occurred while brewing beer", e);
        }
        notifyBottlingService(ingredients, processId);
    }

    private void notifyPresentingService(String correlationId) {
        //Trace scope = this.traceManager.startSpan("calling_prezentatr", correlationId);
        prezentatrClient.dojrzewatr(correlationId);
        //traceManager.close(scope);
    }

    private void notifyBottlingService(Ingredients ingredients, String correlationId) {
        //Trace scope = this.traceManager.startSpan("calling_butelkatr", correlationId);
        bottlingServiceClient.bottle(new Wort(getQuantity(ingredients)), correlationId);
        //traceManager.close(scope);
    }

    private Integer getQuantity(Ingredients ingredients) {
        Assert.notEmpty(ingredients.ingredients);
        return ingredients.ingredients.get(0).getQuantity();
    }

}

