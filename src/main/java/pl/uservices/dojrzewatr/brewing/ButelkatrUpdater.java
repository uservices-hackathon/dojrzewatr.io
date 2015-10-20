package pl.uservices.dojrzewatr.brewing;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.nurkiewicz.asyncretry.RetryExecutor;
import com.ofg.infrastructure.correlationid.CorrelationIdUpdater;
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Trace;
import org.springframework.cloud.sleuth.TraceScope;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import pl.uservices.dojrzewatr.brewing.model.Ingredients;
import pl.uservices.dojrzewatr.brewing.model.Version;
import pl.uservices.dojrzewatr.brewing.model.Wort;

@Slf4j
class ButelkatrUpdater {

    private final ServiceRestClient serviceRestClient;
    private final RetryExecutor retryExecutor;
    private final BrewProperties brewProperties;
    private final Meter brewMeter;
    private final Trace trace;

    public ButelkatrUpdater(ServiceRestClient serviceRestClient, RetryExecutor retryExecutor, BrewProperties brewProperties, MetricRegistry metricRegistry, Trace trace) {
        this.serviceRestClient = serviceRestClient;
        this.retryExecutor = retryExecutor;
        this.brewProperties = brewProperties;
        this.trace = trace;
        this.brewMeter = metricRegistry.meter("brew");
    }

    @Async
    public void updateButelkatrAboutBrewedBeer(final Ingredients ingredients, final String correlationId) {
        CorrelationIdUpdater.updateCorrelationId(correlationId);
        notifyPrezentatr();
        try {
            Long timeout = brewProperties.getTimeout();
            log.info("Brewing beer... it will take [{}] ms", timeout);
            Thread.sleep(timeout);
            brewMeter.mark();
        } catch (InterruptedException e) {
            log.error("Exception occurred while brewing beer", e);
        }
        notifyButelkatr(ingredients);
    }

    private void notifyPrezentatr() {
        TraceScope scope = this.trace.startSpan("calling_prezentatr", new AlwaysSampler(), null);
        serviceRestClient.forService("prezentatr")
                .put().onUrl("/feed/dojrzewatr")
                .withoutBody()
                .withHeaders().contentType(Version.PREZENTATR_V1)
                .andExecuteFor().ignoringResponse();
        scope.close();
    }

    private void notifyButelkatr(Ingredients ingredients) {
        TraceScope scope = this.trace.startSpan("calling_butelkatr", new AlwaysSampler(), null);
        serviceRestClient.forService("butelkatr")
                .post()
                .onUrl("/bottle")
                .body(new Wort(getQuantity(ingredients)))
                .withHeaders().contentType(Version.BUTELKATR_V1)
                .andExecuteFor()
                .ignoringResponse();
        scope.close();
    }

    private Integer getQuantity(Ingredients ingredients) {
        Assert.notEmpty(ingredients.ingredients);
        return ingredients.ingredients.get(0).getQuantity();
    }

}
