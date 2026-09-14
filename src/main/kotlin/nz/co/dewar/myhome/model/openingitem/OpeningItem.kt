package nz.co.dewar.myhome.model.openingitem

import javafx.scene.Group
import javafx.scene.shape.Polygon
import javafx.scene.shape.Shape
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nz.co.dewar.myhome.model.Opening
import nz.co.dewar.myhome.model.SelectableItem
import nz.co.dewar.myhome.model.Wall
import nz.co.dewar.myhome.model.geom.Distance
import kotlin.reflect.KClass

enum class OpeningItemType(val description: String, val clazz: KClass<out OpeningItem>) {
    SLIDING_DOOR("Sliding Door", SlidingDoor::class),
    STANDARD_DOOR("Standard Door", StandardDoor::class),
    STANDARD_WINDOW("Standard Window", StandardWindow::class);

    override fun toString() = description
}

@Serializable
sealed class OpeningItem(
    var width: Distance? = null,
    var height: Distance? = null,
    /** Horizontal offset from the left of the opening */
    var posX: Distance? = null,
    /** Vertical offset from the top of the opening */
    var posY: Distance? = null
) : SelectableItem {
    @Transient
    lateinit var opening: Opening

    abstract val id: String?
    abstract fun render2D(wall: Wall, opening: Opening): Group

    /** Returns the area that can be clicked to select this item */
    fun clickableArea(wall: Wall, opening: Opening): Shape {
        val openingStart = wall.start + wall.unitDirection * opening.edgeDistanceFromWall
        val itemStart = openingStart + wall.unitDirection * posX!!
        val itemEnd = itemStart + wall.unitDirection * width!!
        val halfThicknessNormal = wall.unitDirection.normal() * (wall.thickness / 2)

        val points = listOf(
            itemStart + halfThicknessNormal,
            itemEnd + halfThicknessNormal,
            itemEnd - halfThicknessNormal,
            itemStart - halfThicknessNormal
        )
        return Polygon(*points.flatMap { listOf(it.x, it.y) }.toDoubleArray())
    }
}