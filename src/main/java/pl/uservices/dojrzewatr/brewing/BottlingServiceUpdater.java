package pl.uservices.dojrzewatr.brewing;

import java.net.URI;

import org.springframework.cloud.sleuth.Trace;
import org.springframework.cloud.sleuth.TraceManager;
import org.springframework.cloud.sleuth.trace.TraceContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import pl.uservices.dojrzewatr.brewing.model.Ingredients;
import pl.uservices.dojrzewatr.brewing.model.Version;
import pl.uservices.dojrzewatr.brewing.model.Wort;

@Slf4j
class BottlingServiceUpdater {

    public static final String PROCESS_ID_HEADER = "PROCESS-ID";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";

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
    public void updateBottlingServiceAboutBrewedBeer(final Ingredients ingredients, String processId, TestConfigurationHolder configurationHolder) {
        TestConfigurationHolder.TEST_CONFIG.set(configurationHolder);
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
        Trace scope = this.traceManager.startSpan("calling_presenting", TraceContextHolder.getCurrentSpan());
        switch (TestConfigurationHolder.TEST_CONFIG.get().getTestCommunicationType()) {
            case FEIGN:
                callPresentingViaFeign(correlationId);
                break;
            default:
                useRestTemplateToCallPresenting(correlationId);
        }
        traceManager.close(scope);
    }

    private void callPresentingViaFeign(String correlationId) {
        prezentatrClient.maturingFeed(correlationId);
    }

    private void notifyBottlingService(Ingredients ingredients, String correlationId) {
        Trace scope = this.traceManager.startSpan("calling_bottling", TraceContextHolder.getCurrentSpan());
        switch (TestConfigurationHolder.TEST_CONFIG.get().getTestCommunicationType()) {
            case FEIGN:
                callBottlingViaFeign(ingredients, correlationId);
                break;
            default:
                useRestTemplateToCallBottling(new Wort(getQuantity(ingredients)), correlationId);
        }
        traceManager.close(scope);
    }

    //TODO: Remove duplication
    private void useRestTemplateToCallPresenting(String processId) {
        log.info("Calling presenting - process id [{}]", processId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(PROCESS_ID_HEADER, processId);
        headers.add(CONTENT_TYPE_HEADER, Version.PRESENTING_V1);
        headers.add(TestConfigurationHolder.TEST_COMMUNICATION_TYPE_HEADER_NAME, TestConfigurationHolder.TEST_CONFIG.get().getTestCommunicationType().name());
        String serviceName = "presenting";
        String url = "feed/maturing";
        URI uri = URI.create("http://" + serviceName + "/" + url);
        HttpMethod method = HttpMethod.PUT;
        RequestEntity<String> requestEntity = new RequestEntity<>("Some body", headers, method, uri);
        restTemplate.exchange(requestEntity, String.class);
    }

    private void callBottlingViaFeign(Ingredients ingredients, String correlationId) {
        bottlingServiceClient.bottle(new Wort(getQuantity(ingredients)), correlationId);
    }

    //TODO: Toggle on property or sth
    private void useRestTemplateToCallBottling(Wort wort, String processId) {
        log.info("Calling bottling - process id [{}]", processId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(PROCESS_ID_HEADER, processId);
        headers.add(CONTENT_TYPE_HEADER, Version.BOTTLING_V1);
        headers.add(TestConfigurationHolder.TEST_COMMUNICATION_TYPE_HEADER_NAME, TestConfigurationHolder.TEST_CONFIG.get().getTestCommunicationType().name());
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

