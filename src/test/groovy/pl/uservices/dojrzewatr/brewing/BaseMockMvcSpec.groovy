package pl.uservices.dojrzewatr.brewing
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc
import org.mockito.Mockito
import pl.uservices.dojrzewatr.domain.Warehouse
import pl.uservices.dojrzewatr.domain.Wort
import pl.uservices.dojrzewatr.endpoints.wort.WortEndpoint
import spock.lang.Specification

import static org.mockito.Matchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

abstract class BaseMockMvcSpec extends Specification {

    def setup() {

        def warehouse = mock(Warehouse)

        RestAssuredMockMvc.standaloneSetup(new WortEndpoint(warehouse))
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails()
    }
}
