package pl.uservices.dojrzewatr.brewing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Trace;
import org.springframework.cloud.sleuth.TraceManager;
import org.springframework.cloud.sleuth.trace.TraceContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import pl.uservices.dojrzewatr.brewing.model.Ingredients;
import pl.uservices.dojrzewatr.brewing.model.Wort;

@Slf4j
class ButelkatrUpdater {

    private final BrewProperties brewProperties;
    private final TraceManager traceManager;
    private final PrezentatrClient prezentatrClient;
    private final ButelkatrClient butelkatrClient;

    public ButelkatrUpdater(BrewProperties brewProperties,
                            TraceManager traceManager,
                            PrezentatrClient prezentatrClient,
                            ButelkatrClient butelkatrClient) {
        this.brewProperties = brewProperties;
        this.traceManager = traceManager;
        this.prezentatrClient = prezentatrClient;
        this.butelkatrClient = butelkatrClient;
    }

    @Async
    public void updateButelkatrAboutBrewedBeer(final Ingredients ingredients) {
        Span correlationId = TraceContextHolder.getCurrentSpan();
        notifyPrezentatr(correlationId);
        try {
            Long timeout = brewProperties.getTimeout();
            log.info("Brewing beer... it will take [{}] ms", timeout);
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            log.error("Exception occurred while brewing beer", e);
        }
        notifyButelkatr(ingredients, correlationId);
    }

    private void notifyPrezentatr(Span correlationId) {
        Trace scope = this.traceManager.startSpan("calling_prezentatr", correlationId);
        prezentatrClient.dojrzewatr();
        traceManager.close(scope);
    }

    private void notifyButelkatr(Ingredients ingredients, Span correlationId) {
        Trace scope = this.traceManager.startSpan("calling_butelkatr", correlationId);
        butelkatrClient.bottle(new Wort(getQuantity(ingredients)));
        traceManager.close(scope);
    }

    private Integer getQuantity(Ingredients ingredients) {
        Assert.notEmpty(ingredients.ingredients);
        return ingredients.ingredients.get(0).getQuantity();
    }

}

