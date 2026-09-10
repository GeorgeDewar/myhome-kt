package nz.co.dewar.myhome.model.openingitem

import javafx.scene.Group
import javafx.scene.shape.Polygon
import javafx.scene.shape.Shape
import kotlinx.serialization.Serializable
import nz.co.dewar.myhome.model.Wall
import nz.co.dewar.myhome.graphics2d.Line
import nz.co.dewar.myhome.model.Opening
import nz.co.dewar.myhome.model.SelectableItem

@Serializable
sealed class OpeningItem(
    var width: Double? = null,
    var height: Double? = null,
    /** Horizontal offset from the left of the opening */
    var posX: Double? = null,
    /** Vertical offset from the top of the opening */
    var posY: Double? = null
) : SelectableItem {
    abstract val id: String?
    abstract fun render2D(wall: Wall, opening: Opening): Group

    /** Returns the area that can be clicked to select this item */
    fun clickableArea(wall: Wall, opening: Opening): Shape {
        val openingStart = wall.start + wall.unitDirection * opening.edgeDistanceFromWall / 1000.0
        val itemStart = openingStart + wall.unitDirection * posX!! / 1000.0
        val itemEnd = itemStart + wall.unitDirection * width!! / 1000.0
        val halfThicknessNormal = wall.unitDirection.normal() * (wall.thickness / 2.0)

        val points = listOf(
            itemStart + halfThicknessNormal,
            itemEnd + halfThicknessNormal,
            itemEnd - halfThicknessNormal,
            itemStart - halfThicknessNormal
        )
        return Polygon(*points.flatMap { listOf(it.x, it.y) }.toDoubleArray())
    }
}