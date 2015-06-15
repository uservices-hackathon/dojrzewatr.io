package pl.devoxx.dojrzewatr.brewing;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("brew")
@Data
public class BrewProperties {

    private Integer timeout = 1000;
}
