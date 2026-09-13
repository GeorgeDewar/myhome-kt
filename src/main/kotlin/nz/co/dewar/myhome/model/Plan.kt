package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable

@Serializable
data class Plan(
    val buildings: MutableList<Building>
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
            is Building -> buildings.remove(item)
            is Level -> item.building.levels.remove(item)
            is Wall -> item.level.walls.remove(item)
            is Opening -> item.wall.openings.remove(item)
            else -> throw IllegalArgumentException("Unknown PlanItem type")
        }
    }
}