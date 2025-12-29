package fi.antero.aurorastars.data.model.aurora

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoaaPlanetaryKIndexEntry(
    @SerialName("time_tag")
    val timeTag: String,
    @SerialName("kp_index")
    val kpIndex: Double
)
