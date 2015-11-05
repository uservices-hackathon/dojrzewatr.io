package pl.uservices.dojrzewatr.brewing
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc
import org.mockito.Mockito
import pl.uservices.dojrzewatr.domain.Warehouse
import pl.uservices.dojrzewatr.domain.Wort
import pl.uservices.dojrzewatr.endpoints.wort.WortEndpoint
import spock.lang.Specification

abstract class BaseMockMvcSpec extends Specification {

    def setup() {

        def warehouse = Mockito.mock(Warehouse)

        Mockito.verify(warehouse).addWort(Mockito.any(Wort.class))

        RestAssuredMockMvc.standaloneSetup(new WortEndpoint(warehouse))
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails()
    }

}
