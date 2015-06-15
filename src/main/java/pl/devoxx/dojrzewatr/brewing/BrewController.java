package pl.devoxx.dojrzewatr.brewing;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.devoxx.dojrzewatr.brewing.model.Version;

@RestController
@RequestMapping(value = "/brew", consumes = Version.DOJRZEWATR_V1)
public class BrewController {

     /*   @RequestMapping(method = RequestMethod.POST)
    public Ingredients distributeIngredients(@RequestBody Order order) {
        return ingredientsAggregator.fetchIngredients(order);
    }
*/
}
