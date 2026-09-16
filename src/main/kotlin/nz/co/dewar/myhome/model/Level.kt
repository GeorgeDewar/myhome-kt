package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nz.co.dewar.myhome.model.geom.Distance
import nz.co.dewar.myhome.model.util.InheritedProperty

@Serializable
data class Level(
    val number: Int,
    val name: String,
    val ceilingHeight: InheritedProperty<Distance> = InheritedProperty(Distance(2.400)),
    val totalHeight: InheritedProperty<Distance> = InheritedProperty(Distance(2.545)),
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