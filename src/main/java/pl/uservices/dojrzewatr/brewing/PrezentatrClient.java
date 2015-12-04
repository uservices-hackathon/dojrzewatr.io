package pl.uservices.dojrzewatr.brewing;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.uservices.dojrzewatr.brewing.model.Version;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@FeignClient("prezentatr")
@RequestMapping("/feed")
public interface PrezentatrClient {
    @RequestMapping(
            value = "/dojrzewatr",
            produces = Version.PREZENTATR_V1,
            consumes = Version.PREZENTATR_V1,
            method = PUT)
    String dojrzewatr(@RequestHeader("PROCESS-ID") String processId);
}
