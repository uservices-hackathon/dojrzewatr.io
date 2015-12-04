package pl.uservices.dojrzewatr.brewing;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.uservices.dojrzewatr.brewing.model.Version;
import pl.uservices.dojrzewatr.brewing.model.Wort;

@FeignClient("bottling")
@RequestMapping(value = "/bottle", consumes = Version.BOTTLING_V1,
        produces = MediaType.APPLICATION_JSON_VALUE)
public interface BottlingServiceClient {
    @RequestMapping(method = RequestMethod.POST,
            produces = Version.BOTTLING_V1, consumes = Version.BOTTLING_V1)
    void bottle(Wort wort, @RequestHeader("PROCESS-ID") String processId);
}