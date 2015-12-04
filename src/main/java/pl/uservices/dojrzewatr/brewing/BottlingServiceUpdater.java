package pl.uservices.dojrzewatr.brewing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.TraceManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import pl.uservices.dojrzewatr.brewing.model.Ingredients;
import pl.uservices.dojrzewatr.brewing.model.Version;
import pl.uservices.dojrzewatr.brewing.model.Wort;

import java.net.URI;

@Slf4j
class BottlingServiceUpdater {

    private final BrewProperties brewProperties;
    private final TraceManager traceManager;
    private final PresentingServiceClient prezentatrClient;
    private final BottlingServiceClient bottlingServiceClient;
    private final RestTemplate restTemplate;

    public BottlingServiceUpdater(BrewProperties brewProperties,
                                  TraceManager traceManager,
                                  PresentingServiceClient prezentatrClient,
                                  BottlingServiceClient bottlingServiceClient,
                                  RestTemplate restTemplate) {
        this.brewProperties = brewProperties;
        this.traceManager = traceManager;
        this.prezentatrClient = prezentatrClient;
        this.bottlingServiceClient = bottlingServiceClient;
        this.restTemplate = restTemplate;
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
        //callPresentingViaFeign(correlationId);
        //traceManager.close(scope);
        useRestTemplateToCallPresenting(correlationId);
    }

    private void callPresentingViaFeign(String correlationId) {
        prezentatrClient.dojrzewatr(correlationId);
    }

    private void notifyBottlingService(Ingredients ingredients, String correlationId) {
        //Trace scope = this.traceManager.startSpan("calling_butelkatr", correlationId);
        //callBottlingViaFeign(ingredients, correlationId);
        //traceManager.close(scope);
        useRestTemplateToCallBottling(new Wort(getQuantity(ingredients)), correlationId);
    }


    //TODO: Toggle on property or sth
    private void useRestTemplateToCallPresenting(String processId) {
        log.info("Calling presenting - process id [{}]", processId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("PROCESS-ID", processId);
        headers.add("Content-Type", Version.PRESENTING_V1);
        String serviceName = "presenting";
        String url = "feed/maturing";
        URI uri = URI.create("http://" + serviceName + "/" + url);
        HttpMethod method = HttpMethod.PUT;
        RequestEntity<String> requestEntity = new RequestEntity<>("SOme body", headers, method, uri);
        restTemplate.exchange(requestEntity, String.class);
    }

    private void callBottlingViaFeign(Ingredients ingredients, String correlationId) {
        bottlingServiceClient.bottle(new Wort(getQuantity(ingredients)), correlationId);
    }

    //TODO: Toggle on property or sth
    private void useRestTemplateToCallBottling(Wort wort, String processId) {
        log.info("Calling bottling - process id [{}]", processId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("PROCESS-ID", processId);
        headers.add("Content-Type", Version.BOTTLING_V1);
        String serviceName = "bottling";
        String url = "bottle";
        URI uri = URI.create("http://" + serviceName + "/" + url);
        HttpMethod method = HttpMethod.POST;
        RequestEntity<Wort> requestEntity = new RequestEntity<>(wort, headers, method, uri);
        restTemplate.exchange(requestEntity, String.class);
    }

    private Integer getQuantity(Ingredients ingredients) {
        Assert.notEmpty(ingredients.ingredients);
        return ingredients.ingredients.get(0).getQuantity();
    }

}

