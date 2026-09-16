package nz.co.dewar.myhome.model

import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nz.co.dewar.myhome.graphics2d.Polygon
import nz.co.dewar.myhome.graphics2d.toJtsPolygon
import nz.co.dewar.myhome.model.geom.Point2D
import org.locationtech.jts.algorithm.construct.MaximumInscribedCircle
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Serializable
data class Room(
    var name: String,
    val walls: MutableList<RoomWall>,
) : SelectableItem {
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
                val wall1Line = wall1.centerLine
                val wall2Line = wall2.centerLine
                val intersection = wall1Line.intersection(wall2Line)
                    ?: throw IllegalArgumentException("Walls '${wall1.id}' and '${wall2.id}' of room '$name' do not intersect")
                intersections.add(intersection)
                logger.trace("Found intersection of walls '${wall1.id}' and '${wall2.id}' of room '$name': $intersection")
            }

            return Polygon(*intersections.toTypedArray())
        }

    val label: Node
        get() {
            val areaPolygon = internalArea
            val maximumInscribedCircle = MaximumInscribedCircle(areaPolygon.toJtsPolygon(), 0.01)

            val roomName = Text(maximumInscribedCircle.center.x, maximumInscribedCircle.center.y, name).apply {
                textAlignment = TextAlignment.CENTER
                textOrigin = VPos.TOP
                font = Font.font(0.3)
                applyCss()
                x = maximumInscribedCircle.center.x - layoutBounds.width / 2
                y = maximumInscribedCircle.center.y - layoutBounds.height / 2
                while (!areaPolygon.boundsInLocal.contains(layoutBounds) && font.size > 0.1) {
                    font = Font.font(font.size * 0.8)
                    applyCss()
                    x = maximumInscribedCircle.center.x - layoutBounds.width / 2
                    y = maximumInscribedCircle.center.y - layoutBounds.height / 2
                }
            }

            return Group(roomName)
        }

    override val treeLabel get() = name
}