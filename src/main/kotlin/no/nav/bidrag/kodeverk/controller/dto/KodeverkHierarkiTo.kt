package no.nav.bidrag.kodeverk.controller.dto

data class KodeverkHierarkiResponse(
    val hierarkinivaaer: List<String>,
    val noder: Map<String, Hierarkinode>,
)

data class Hierarkinode(
    var kode: String? = null,
    var termer: MutableMap<String, String> = mutableMapOf(),
    var undernoder: MutableMap<String, Hierarkinode> = mutableMapOf(),
)
