package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Room(
    val name: String,
    val walls: MutableList<RoomWall>,
) : PlanItem {
    @Transient
    lateinit var level: Level

    init {
        walls.forEach { wall -> wall.room = this }
    }

    override val treeLabel = name
}