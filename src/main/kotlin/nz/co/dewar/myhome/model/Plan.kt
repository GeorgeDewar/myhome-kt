package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable

@Serializable
data class Plan(
    val buildings: List<Building>
) {
    fun getWallsOnLevel(level: Int): List<Wall> {
        return buildings.flatMap { building ->
            building.levels.find { it.number == level }?.walls ?: emptyList()
        }
    }
}