package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable

@Serializable
data class Plan(
    val buildings: List<Building>
) : PlanItem {
    override val treeLabel = "Plan"

    init {
        buildings.forEach { building -> building.plan = this }
    }

    fun getWallsOnLevel(level: Int): List<Wall> {
        return buildings.flatMap { building ->
            building.levels.find { it.number == level }?.walls ?: emptyList()
        }
    }

    fun deleteItem(item: PlanItem) {
        when (item) {
            is Building -> buildings.toMutableList().remove(item)
            is Level -> item.building.levels.toMutableList().remove(item)
            is Wall -> item.level.walls.toMutableList().remove(item)
            is Opening -> item.wall.openings.toMutableList().remove(item)
            else -> throw IllegalArgumentException("Unknown PlanItem type")
        }
    }
}