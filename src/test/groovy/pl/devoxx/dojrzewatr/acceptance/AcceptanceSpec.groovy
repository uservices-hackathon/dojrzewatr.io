package pl.devoxx.dojrzewatr.acceptance

import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MvcResult
import pl.devoxx.dojrzewatr.base.MicroserviceMvcWiremockSpec
import pl.devoxx.dojrzewatr.brewing.model.Version

import static java.net.URI.create
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@ContextConfiguration
class AcceptanceSpec extends MicroserviceMvcWiremockSpec {

    def 'should mellow the beer and call butelkatr.io that beer is ready'() {
        when:
            MvcResult result = brewing_the_beer()
        then:
            butelkatr_will_be_called_that_beer_is_ready(result)
    }

    private MvcResult brewing_the_beer() {
        return mockMvc.perform(post(create('/brew'))
                .header('Content-Type', Version.DOJRZEWATR_V1)
                .content('''
                {
                    "ingredients": [
                            {"type":"MALT","quantity":1000},
                            {"type":"WATER","quantity":1000},
                            {"type":"HOP","quantity":1000},
                            {"type":"YIEST","quantity":1000}
                        ]
                }
                '''))
                .andDo(print())
                .andReturn()
    }

    private void butelkatr_will_be_called_that_beer_is_ready(MvcResult result) {
        assert !result.resolvedException
    }

}
