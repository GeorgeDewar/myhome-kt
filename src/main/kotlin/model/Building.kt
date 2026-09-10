package model

import kotlinx.serialization.Serializable

@Serializable
data class Building(
    val name: String,
    val levels: List<Floor>
)