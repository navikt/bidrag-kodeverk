import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import no.nav.bidrag.commons.service.KodeverkKoderBetydningerResponse
import org.junit.Assert
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class StubUtils {
    companion object {
        fun aClosedJsonResponse(): ResponseDefinitionBuilder =
            aResponse()
                .withHeader(HttpHeaders.CONNECTION, "close")
                .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    }

    fun stubKodeverkResponse(response: KodeverkKoderBetydningerResponse) {
        try {
            WireMock.stubFor(
                WireMock.get(urlMatching("/kodeverk/(.*)")).willReturn(
                    aClosedJsonResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(ObjectMapper().findAndRegisterModules().registerKotlinModule().writeValueAsString(response)),
                ),
            )
        } catch (e: JsonProcessingException) {
            Assert.fail(e.message)
        }
    }
}
