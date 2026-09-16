package nz.co.dewar.myhome.model

import javafx.scene.Group
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nz.co.dewar.myhome.graphics2d.Polygon
import nz.co.dewar.myhome.model.geom.Point2D
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Serializable
data class Room(
    val name: String,
    val walls: MutableList<RoomWall>,
) : PlanItem {
    private val logger: Logger = LoggerFactory.getLogger(Room::class.java)

    @Transient
    lateinit var level: Level

    init {
        walls.forEach { wall -> wall.room = this }
    }

    val internalArea: Polygon
        get() {
            val intersections = mutableListOf<Point2D>()
            for (i in walls.indices) {
                val wall1 = walls[i].wall
                val wall2 = walls[(i + 1) % walls.size].wall
                val wall1Line = wall1.centerLine //.extendedBy(wall1.thickness.value / 2)
                val wall2Line = wall2.centerLine //.extendedBy(wall2.thickness.value / 2)
                val intersection = wall1Line.intersection(wall2Line)
                    ?: throw IllegalArgumentException("Walls '${wall1.id}' and '${wall2.id}' of room '$name' do not intersect")
                intersections.add(intersection)
                logger.trace("Found intersection of walls '${wall1.id}' and '${wall2.id}' of room '$name': $intersection")
            }

            return Polygon(*intersections.toTypedArray())
        }

    val label: Group
        get() {
            val areaPolygon = internalArea
            val boundingBox = areaPolygon.boundsInLocal
            val centroid = Point2D(boundingBox.minX + boundingBox.width / 2, boundingBox.minY + boundingBox.height / 2)

            val roomName = Text(centroid.x, centroid.y, name).apply {
                textAlignment = TextAlignment.CENTER
                font = Font.font(0.4)
            }
            roomName.applyCss()
            roomName.x = centroid.x - roomName.layoutBounds.width / 2
            roomName.y = centroid.y + roomName.layoutBounds.height / 2

            val stackPane = Group(roomName)
            return stackPane
        }

    override val treeLabel = name
}