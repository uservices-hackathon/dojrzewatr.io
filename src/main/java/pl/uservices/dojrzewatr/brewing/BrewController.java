package pl.uservices.dojrzewatr.brewing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.uservices.dojrzewatr.brewing.model.Ingredients;
import pl.uservices.dojrzewatr.brewing.model.Version;

@RestController
@RequestMapping(value = "/brew", consumes = Version.MATURING_V1)
public class BrewController {

    private final BottlingServiceUpdater bottlingServiceUpdater;

    @Autowired
    public BrewController(BottlingServiceUpdater bottlingServiceUpdater) {
        this.bottlingServiceUpdater = bottlingServiceUpdater;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void distributeIngredients(@RequestBody Ingredients ingredients, @RequestHeader("PROCESS-ID") String processId) {
        bottlingServiceUpdater.updateBottlingServiceAboutBrewedBeer(ingredients, processId);
    }
}
