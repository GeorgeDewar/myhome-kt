package nz.co.dewar.myhome.graphics2d

import javafx.beans.binding.Bindings
import javafx.scene.paint.Color
import javafx.scene.shape.ClosePath
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.scene.shape.Shape
import nz.co.dewar.myhome.model.Building
import nz.co.dewar.myhome.model.Wall

class WallRenderer(val building: Building) {
    fun getWallsShape(): Shape {
        val floor = building.levels[0]
        var wallArea: Shape? = null
        for (wall in floor.walls) {
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