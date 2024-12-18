package no.nav.bidrag.kodeverk.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.bidrag.commons.CorrelationId
import no.nav.bidrag.commons.cache.BrukerCacheable
import no.nav.bidrag.commons.web.client.AbstractRestClient
import no.nav.bidrag.kodeverk.config.CacheConfig.Companion.KODEVERK_CACHE
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.time.LocalDate

private val log = KotlinLogging.logger {}

data class KodeverkKoderBetydningerResponse(
    val betydninger: Map<String, List<KodeverkBetydning>> = emptyMap(),
)

data class KodeverkBetydning(
    val gyldigFra: LocalDate,
    val gyldigTil: LocalDate,
    val beskrivelser: Map<String, KodeverkBeskrivelse>,
)

data class KodeverkBeskrivelse(
    val tekst: String,
    val term: String,
)

@Service
class KodeverkConsumer(
    @Value("\${KODEVERK_URL}") val url: URI,
    @Value("\${NAIS_APP_NAME}:bidrag-commons") val appName: String,
    @Qualifier("azure") restTemplate: RestTemplate,
) : AbstractRestClient(restTemplate, "kodeverk-api") {
    private val kodeverkUri get() =
        UriComponentsBuilder
            .fromUri(url)
            .pathSegment("api", "v1", "kodeverk")

    @BrukerCacheable(KODEVERK_CACHE)
    fun hentKodeverk(kodeverk: String): KodeverkKoderBetydningerResponse {
        log.info("Henter kodeverk for $kodeverk")
        val header = HttpHeaders()
        header.add("Nav-Call-Id", CorrelationId.fetchCorrelationIdForThread())
        header.add("Nav-Consumer-Id", appName)
        return getForEntity(
            kodeverkUri
                .pathSegment(
                    kodeverk,
                    "koder",
                    "betydninger",
                ).queryParam("ekskluderUgyldige", "false")
                .queryParam("spraak", "nb")
                .build()
                .toUri(),
            header,
        )!!
    }
}
