package pl.devoxx.dojrzewatr.brewing
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc

import spock.lang.Specification

abstract class BaseMockMvcSpec extends Specification {

    protected static final int QUANTITY = 200

    IngredientsAggregator ingredientsAggregator = Stub()

    def setup() {
        setupMocks()
        RestAssuredMockMvc.standaloneSetup(new BrewController(ingredientsAggregator))
    }

    void setupMocks() {
        ingredientsAggregator.fetchIngredients(_) >> { Order order ->
            return new Ingredients(order.items.collect { new Ingredient(it, QUANTITY)})
        }
    }

}
