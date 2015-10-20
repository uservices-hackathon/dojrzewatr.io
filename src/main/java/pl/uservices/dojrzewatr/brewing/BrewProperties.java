package pl.uservices.dojrzewatr.brewing;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("brew")
@Data
public class BrewProperties {

    private Long timeout = 500L;
}
