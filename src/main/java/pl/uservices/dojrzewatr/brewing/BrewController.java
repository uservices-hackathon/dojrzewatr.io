package pl.uservices.dojrzewatr.brewing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public void distributeIngredients(@RequestBody Ingredients ingredients, @RequestHeader("PROCESS-ID") String processId) {
        butelkatrUpdater.updateButelkatrAboutBrewedBeer(ingredients, processId);
    }
}
