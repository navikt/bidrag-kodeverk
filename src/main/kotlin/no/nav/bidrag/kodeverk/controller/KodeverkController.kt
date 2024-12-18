package no.nav.bidrag.kodeverk.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import no.nav.bidrag.kodeverk.consumer.KodeverkKoderBetydningerResponse
import no.nav.bidrag.kodeverk.service.KodeverKService
import no.nav.security.token.support.core.api.Unprotected
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Unprotected
class KodeverkController(
    val kodeverKService: KodeverKService,
) {
    @GetMapping("/kodeverk/{kodeverk}")
    @Operation(
        description = "Henter kodeverk (proxy)",
        security = [SecurityRequirement(name = "bearer-key")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Hentet kodeverk"),
        ],
    )
    fun hentKodeverk(
        @PathVariable kodeverk: String,
    ): KodeverkKoderBetydningerResponse = kodeverKService.hentKodeverk(kodeverk)
}
