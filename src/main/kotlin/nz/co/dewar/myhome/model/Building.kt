package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable

@Serializable
data class Building(
    val name: String,
    val levels: List<Level>
)