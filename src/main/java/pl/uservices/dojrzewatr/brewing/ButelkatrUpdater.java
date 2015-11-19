package pl.uservices.dojrzewatr.brewing;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.nurkiewicz.asyncretry.AsyncRetryExecutor;
import com.nurkiewicz.asyncretry.RetryExecutor;
import com.ofg.infrastructure.correlationid.CorrelationIdHolder;
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Trace;
import org.springframework.cloud.sleuth.TraceScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import pl.uservices.dojrzewatr.brewing.model.Ingredients;
import pl.uservices.dojrzewatr.brewing.model.Version;
import pl.uservices.dojrzewatr.brewing.model.Wort;

import java.util.concurrent.Executors;

@Slf4j
class ButelkatrUpdater {

    private final ServiceRestClient serviceRestClient;
    private final RetryExecutor retryExecutor;
    private final BrewProperties brewProperties;
    private final Meter brewMeter;
    private final Trace trace;

    public ButelkatrUpdater(ServiceRestClient serviceRestClient, BrewProperties brewProperties, MetricRegistry metricRegistry, Trace trace) {
        this.serviceRestClient = serviceRestClient;
        this.retryExecutor = new AsyncRetryExecutor(Executors.newSingleThreadScheduledExecutor()).dontRetry();
        this.brewProperties = brewProperties;
        this.trace = trace;
        this.brewMeter = metricRegistry.meter("brew");
    }

    @Async
    public void updateButelkatrAboutBrewedBeer(final Ingredients ingredients) {
        Span correlationId = CorrelationIdHolder.get();
        notifyPrezentatr(correlationId);
        try {
            Long timeout = brewProperties.getTimeout();
            log.info("Brewing beer... it will take [{}] ms", timeout);
            Thread.sleep(timeout);
            brewMeter.mark();
        } catch (InterruptedException e) {
            log.error("Exception occurred while brewing beer", e);
        }
        notifyButelkatr(ingredients, correlationId);
    }

    private void notifyPrezentatr(Span correlationId) {
        TraceScope scope = this.trace.startSpan("calling_prezentatr", correlationId);
        serviceRestClient.forService("prezentatr")
                .retryUsing(retryExecutor)
                .put().onUrl("/feed/dojrzewatr")
                .withoutBody()
                .withHeaders().contentType(Version.PREZENTATR_V1)
                .andExecuteFor().ignoringResponseAsync();
        scope.close();
    }

    private void notifyButelkatr(Ingredients ingredients, Span correlationId) {
        TraceScope scope = this.trace.startSpan("calling_butelkatr", correlationId);
        serviceRestClient.forService("butelkatr")
                .retryUsing(retryExecutor)
                .post()
                .onUrl("/bottle")
                .body(new Wort(getQuantity(ingredients)))
                .withHeaders().contentType(Version.BUTELKATR_V1)
                .andExecuteFor()
                .ignoringResponseAsync();
        scope.close();
    }

    private Integer getQuantity(Ingredients ingredients) {
        Assert.notEmpty(ingredients.ingredients);
        return ingredients.ingredients.get(0).getQuantity();
    }

}
