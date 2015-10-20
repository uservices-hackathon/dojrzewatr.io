package pl.uservices.dojrzewatr.brewing;

import com.ofg.infrastructure.correlationid.CorrelationIdHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.uservices.dojrzewatr.brewing.model.Ingredients;
import pl.uservices.dojrzewatr.brewing.model.Version;

@RestController
@RequestMapping(value = "/brew", consumes = Version.DOJRZEWATR_V1)
public class BrewController {

    private final ButelkatrUpdater butelkatrUpdater;

    @Autowired
    public BrewController(ButelkatrUpdater butelkatrUpdater) {
        this.butelkatrUpdater = butelkatrUpdater;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void distributeIngredients(@RequestBody Ingredients ingredients) {
        butelkatrUpdater.updateButelkatrAboutBrewedBeer(ingredients, CorrelationIdHolder.get());
    }
}
