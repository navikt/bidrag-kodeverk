package no.nav.bidrag.kodeverk.service

import no.nav.bidrag.commons.service.KodeverkKoderBetydningerResponse
import no.nav.bidrag.kodeverk.consumer.KodeverkConsumer
import no.nav.bidrag.kodeverk.controller.dto.KodeverkHierarkiResponse
import org.springframework.stereotype.Service

@Service
class KodeverkService(
    val kodeverkConsumer: KodeverkConsumer,
) {
    fun hentKodeverk(kodeverk: String): KodeverkKoderBetydningerResponse = kodeverkConsumer.hentKodeverk(kodeverk)

    fun hentKodeverkHiearki(kodeverk: String): KodeverkHierarkiResponse = kodeverkConsumer.hentKodeverHierarki(kodeverk)
}
