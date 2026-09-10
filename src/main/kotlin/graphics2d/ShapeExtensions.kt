package org.example.graphics2d

import javafx.scene.shape.Line
import javafx.scene.shape.Polygon
import org.example.model.geom.Point2D
import org.example.model.geom.Vector2D

fun Line(start: Point2D, end: Point2D): Line {
    return Line(start.x, start.y, end.x, end.y)
}

operator fun Line.plus(vector2D: Vector2D): Line {
    return Line(Point2D(this.startX + vector2D.dX, this.startY + vector2D.dY),
        Point2D(this.endX + vector2D.dX, this.endY + vector2D.dY))
}

operator fun Line.minus(vector2D: Vector2D): Line {
    return Line(Point2D(this.startX - vector2D.dX, this.startY - vector2D.dY),
        Point2D(this.endX - vector2D.dX, this.endY - vector2D.dY))
}

fun Polygon(vararg points: Point2D): Polygon {
    return Polygon(*points.flatMap { listOf(it.x, it.y) }.toDoubleArray())
}