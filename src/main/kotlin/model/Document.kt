package model

import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val buildings: List<Building>
) {
    fun getWallsOnLevel(level: Int): List<Wall> {
        return buildings.flatMap { building ->
            building.levels.find { it.number == level }?.walls ?: emptyList()
        }
    }
}