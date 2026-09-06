package no.nav.bidrag.kodeverk.controller.dto

data class KodeverkHierarkiResponse(
    val hierarkinivaaer: List<String>,
    val noder: Map<String, Hierarkinode>,
)

data class Hierarkinode(
    val kode: String? = null,
    val termer: MutableMap<String, String> = mutableMapOf(),
    val undernoder: MutableMap<String, Hierarkinode> = mutableMapOf(),
)
