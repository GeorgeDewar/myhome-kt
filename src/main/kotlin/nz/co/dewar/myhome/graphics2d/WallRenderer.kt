package nz.co.dewar.myhome.graphics2d

import javafx.scene.paint.Color
import javafx.scene.shape.Path
import javafx.scene.shape.Shape
import nz.co.dewar.myhome.model.Wall

object WallRenderer {
    /** Render a collection of walls as a shape, taking into account its openings */
    fun renderWalls(walls: List<Wall>): Shape {
        var wallArea: Shape? = null
        for (wall in walls) {
            var path: Shape = wall.areaPolygon

            for (opening in wall.openings) {
                val openingPath = wall.openingPolygon(opening)
                path = Shape.subtract(path, openingPath)
            }

            wallArea = if (wallArea != null) {
                Shape.union(wallArea, path)
            } else {
                path
            }
        }

        if (wallArea == null) {
            wallArea = Path()
        }
        wallArea.fill = Color.rgb(255, 215, 0)
        wallArea.strokeWidth = 0.01
        wallArea.stroke = Color.BLACK

        return wallArea
    }
}