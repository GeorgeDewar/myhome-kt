package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Level(
    val number: Int,
    val name: String,
    val walls: List<Wall> = emptyList(),
    val rooms: List<Room> = emptyList()
) : PlanItem {
    override val treeLabel = name

    @Transient
    lateinit var building: Building

    init {
        walls.forEach { wall -> wall.level = this }
    }
}