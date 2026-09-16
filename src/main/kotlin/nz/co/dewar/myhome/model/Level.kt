package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nz.co.dewar.myhome.model.geom.Distance
import nz.co.dewar.myhome.model.util.InheritedProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Serializable
data class Level(
    val number: Int,
    val name: String,
    val ceilingHeight: InheritedProperty<Distance> = InheritedProperty(Distance(2.400)),
    val totalHeight: InheritedProperty<Distance> = InheritedProperty(Distance(2.545)),
    val walls: MutableList<Wall> = mutableListOf(),
    val rooms: MutableList<Room> = mutableListOf()
) : PlanItem {
    private val logger: Logger = LoggerFactory.getLogger(Level::class.java)

    override val treeLabel = name

    @Transient
    lateinit var building: Building

    init {
        walls.forEach { wall -> wall.level = this }
        for (room in rooms) {
            room.level = this
            for (roomWall in room.walls) {
                // Find the wall
                val wall = walls.find { it.id == roomWall.ref }
                if (wall != null) {
                    roomWall.wall = wall
                } else {
                    throw IllegalArgumentException("Wall with id '${roomWall.ref}' not found in level '$name'")
                }
            }

            logger.debug("Room '${room.name}'")
            try {
                room.internalArea
            } catch (e: Exception) {
                logger.error("Error calculating internal area for room '${room.name}' in level '$name': ${e.message}")
            }
        }
    }

    fun removeWall(wall: Wall) {
        if (rooms.any { it.walls.any { roomWall -> roomWall.wall == wall } }) {
            throw IllegalArgumentException("Cannot remove wall '${wall.id}' because it is referenced by a room")
        }
        walls.remove(wall)
    }
}