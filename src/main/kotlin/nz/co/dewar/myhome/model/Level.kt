package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Level(
    val number: Int,
    val name: String,
    val walls: MutableList<Wall> = mutableListOf(),
    val rooms: MutableList<Room> = mutableListOf()
) : PlanItem {
    override val treeLabel = name

    @Transient
    lateinit var building: Building

    init {
        walls.forEach { wall -> wall.level = this }
    }
}