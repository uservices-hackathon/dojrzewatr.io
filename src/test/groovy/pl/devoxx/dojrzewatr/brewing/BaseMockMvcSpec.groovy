package pl.devoxx.dojrzewatr.brewing
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc
import spock.lang.Specification

abstract class BaseMockMvcSpec extends Specification {

    ButelkatrUpdater butelkatrUpdater = Stub()

    def setup() {
        RestAssuredMockMvc.standaloneSetup(new BrewController(butelkatrUpdater))
    }

}
