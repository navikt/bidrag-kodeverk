package no.nav.bidrag.kodeverk.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.bidrag.commons.CorrelationId
import no.nav.bidrag.commons.cache.BrukerCacheable
import no.nav.bidrag.commons.service.KodeverkKoderBetydningerResponse
import no.nav.bidrag.commons.web.client.AbstractRestClient
import no.nav.bidrag.kodeverk.config.CacheConfig.Companion.KODEVERK_CACHE
import no.nav.bidrag.kodeverk.controller.dto.KodeverkHierarkiResponse
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
    @Value("\${KODEVERK_URL}") val url: URI,
    @Value("\${NAIS_APP_NAME}:bidrag-commons") val appName: String,
    @Qualifier("azure") restTemplate: RestTemplate,
) : AbstractRestClient(restTemplate, "kodeverk-api") {
    private val kodeverkUri get() =
        UriComponentsBuilder
            .fromUri(url)
            .pathSegment("api", "v1", "kodeverk")
    private val hierarkiUri get() =
        UriComponentsBuilder
            .fromUri(url)
            .pathSegment("api", "v1", "hierarki")

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
                ).queryParam("inkluderUtkast", "false")
                .queryParam("ekskluderUgyldige", "false")
                .queryParam("spraak", "nb")
                .build()
                .toUri(),
            header,
        )!!
    }

    @BrukerCacheable(KODEVERK_CACHE)
    fun hentKodeverHierarki(kodeverk: String): KodeverkHierarkiResponse {
        log.info("Henter hierarki for $kodeverk")
        val header = HttpHeaders()
        header.add("Nav-Call-Id", CorrelationId.fetchCorrelationIdForThread())
        header.add("Nav-Consumer-Id", appName)
        return getForEntity(
            hierarkiUri
                .pathSegment(
                    kodeverk,
                    "noder",
                ).queryParam("inkluderUtkast", "false")
                .queryParam("ekskluderUgyldige", "false")
                .queryParam("spraak", "nb")
                .build()
                .toUri(),
            header,
        )!!
    }
}
