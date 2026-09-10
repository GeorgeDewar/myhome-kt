package org.example.model.openingitem

import javafx.scene.Group
import javafx.scene.paint.Color
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.model.Wall
import org.example.model.Opening
import org.example.model.OpeningType
import org.example.graphics2d.Line
import org.example.graphics2d.minus
import org.example.graphics2d.plus

@Serializable
@SerialName("StandardWindow")
class StandardWindow(override val id: String? = null) : OpeningItem() {
    override fun render2D(wall: Wall, opening: Opening): Group {
        check(opening.type == OpeningType.WINDOW) { "Opening must be of type WINDOW" }

        val lineThickness = 0.01
        val windowStart = wall.start + wall.unitDirection * opening.edgeDistanceFromWall / 1000.0
        val windowEnd = windowStart + wall.unitDirection * opening.width / 1000.0
        val halfThicknessNormal = wall.unitDirection.normal() * (wall.thickness / 2.0)

        val centerLine = Line(windowStart, windowEnd)
        val lineA = centerLine + halfThicknessNormal
        val lineB = centerLine - halfThicknessNormal

        listOf(centerLine, lineB, lineA).forEach {
            it.strokeWidth = lineThickness
            it.fill = null
            it.stroke = Color.BLACK
        }

        return Group(centerLine, lineA, lineB)
    }
}