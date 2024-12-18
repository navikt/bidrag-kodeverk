package no.nav.bidrag.kodeverk.service

import no.nav.bidrag.kodeverk.consumer.KodeverkConsumer
import no.nav.bidrag.kodeverk.consumer.KodeverkKoderBetydningerResponse
import org.springframework.stereotype.Service

@Service
class KodeverKService(
    val kodeverkConsumer: KodeverkConsumer,
) {
    fun hentKodeverk(kodeverk: String): KodeverkKoderBetydningerResponse = kodeverkConsumer.hentKodeverk(kodeverk)
}
