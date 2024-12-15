package no.nav.bidrag.kodeverk.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.bidrag.commons.CorrelationId
import no.nav.bidrag.commons.cache.BrukerCacheable
import no.nav.bidrag.commons.service.KodeverkKoderBetydningerResponse
import no.nav.bidrag.commons.web.client.AbstractRestClient
import no.nav.bidrag.kodeverk.config.CacheConfig.Companion.KODEVERK_CACHE
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

private val log = KotlinLogging.logger {}

@Service
class KodeverkConsumer(
    @Value("\${KODEVERK_URL}") kodeverkUri: URI,
    @Value("\${NAIS_APP_NAME}:bidrag-commons") val appName: String,
    @Qualifier("azure") restTemplate: RestTemplate,
) : AbstractRestClient(restTemplate, "kodeverk-api") {
    private val kodeverkUri =
        UriComponentsBuilder
            .fromUri(kodeverkUri)
            .pathSegment("api", "v1", "kodeverk")

    @BrukerCacheable(KODEVERK_CACHE)
    fun hentKodeverk(kodeverk: String): KodeverkKoderBetydningerResponse {
        log.info("Henter kodeverk for $kodeverk")
        val header = HttpHeaders()
        header.add("Nav-Call-Id", CorrelationId.fetchCorrelationIdForThread())
        header.add("Nav-Consumer-Id", appName)
        return getForNonNullEntity(
            kodeverkUri
                .pathSegment(
                    kodeverk,
                    "kodeverk",
                    "betydninger",
                ).queryParam("ekskluderUgyldige", "false")
                .queryParam("spraak", "nb")
                .build()
                .toUri(),
        )
    }
}
