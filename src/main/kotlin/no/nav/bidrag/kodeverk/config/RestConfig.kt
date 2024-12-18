package no.nav.bidrag.kodeverk.config

import no.nav.bidrag.commons.web.config.RestOperationsAzure
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.client.observation.ClientRequestObservationConvention
import org.springframework.http.client.observation.DefaultClientRequestObservationConvention

@Configuration
@Import(RestOperationsAzure::class)
class RestConfig {
    @Bean
    fun clientRequestObservationConvention(): ClientRequestObservationConvention = DefaultClientRequestObservationConvention()
}
