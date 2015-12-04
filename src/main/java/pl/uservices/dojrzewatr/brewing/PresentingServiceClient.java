package pl.uservices.dojrzewatr.brewing;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.uservices.dojrzewatr.brewing.model.Version;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@FeignClient("presenting")
@RequestMapping("/feed")
public interface PresentingServiceClient {
    @RequestMapping(
            value = "/maturing",
            produces = Version.PRESENTING_V1,
            consumes = Version.PRESENTING_V1,
            method = PUT)
    String dojrzewatr(@RequestHeader("PROCESS-ID") String processId);
}
