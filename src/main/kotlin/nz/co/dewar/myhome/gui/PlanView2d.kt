package nz.co.dewar.myhome.gui

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import nz.co.dewar.myhome.graphics2d.WallRenderer
import nz.co.dewar.myhome.gui.ApplicationContext.level
import nz.co.dewar.myhome.gui.ApplicationContext.plan
import nz.co.dewar.myhome.gui.ApplicationContext.selectedItems
import org.slf4j.LoggerFactory

class PlanView2d {
    private val logger = LoggerFactory.getLogger(PlanView2d::class.java)
    val pane = Pane()

    var onUpdate: (() -> Unit)? = null

    private val content = Group()
    var scale = SimpleDoubleProperty(20.0)
    private var lastMouseInPane = Point2D(0.0, 0.0)
    private var lastMouseInScene = Point2D(0.0, 0.0)

    val cursorPositionWorld: Point2D
        get() = content.sceneToLocal(lastMouseInScene)

    val offset: Point2D
        get() {
            val paneOriginInScene = pane.localToScene(0.0, 0.0) ?: Point2D(0.0, 0.0)
            return content.sceneToLocal(paneOriginInScene)
        }

    init {
        // Keep the drawing area constrained to the center pane so it cannot paint over the
        // MenuBar/ToolBar region in the BorderPane.
        val clip = Rectangle().apply {
            widthProperty().bind(pane.widthProperty())
            heightProperty().bind(pane.heightProperty())
        }
        pane.clip = clip

        // So we know we're rendering something
        //pane.background = Background(BackgroundFill(Color.LIGHTGRAY, null, null))

        // Set initial scale
        content.scaleX = scale.get()
        content.scaleY = scale.get()
        content.translateX = 200.0
        content.translateY = 200.0

        val circle = Circle(0.0, 0.0, 1.0, Color.BLUE)
        content.children.add(circle)
        pane.children.add(content)

        pane.setOnScroll { event ->
            val zoomFactor = if (event.deltaY > 0) 1.1 else 1.0 / 1.1
            val oldScale = scale
            val newScale = (oldScale.get() * zoomFactor).coerceIn(20.0, 200.0)
            if (newScale == oldScale.get()) {
                return@setOnScroll
            }

            val pivotInContent = content.sceneToLocal(event.sceneX, event.sceneY)

            scale.set(newScale)
            content.scaleX = scale.get()
            content.scaleY = scale.get()

            val pivotAfterZoom = content.localToScene(pivotInContent)
            content.translateX += event.sceneX - pivotAfterZoom.x
            content.translateY += event.sceneY - pivotAfterZoom.y
            event.consume()

            onUpdate?.invoke()
        }

        pane.setOnMousePressed { event ->
            if (event.isPrimaryButtonDown) {
                lastMouseInPane = Point2D(event.x, event.y)
                lastMouseInScene = Point2D(event.sceneX, event.sceneY)
            }
        }

        pane.onMouseClicked = { event ->
            if (event.button == MouseButton.PRIMARY) {
                selectedItems.clear()

                val clickedPointInContent = content.sceneToLocal(event.sceneX, event.sceneY)
                for (wall in plan.getWallsOnLevel(level)) {
                    // Check if the clicked point is on the wall
                    if (wall.areaPolygon.contains(clickedPointInContent)) {
                        selectedItems.add(wall)

                        // Check if the clicked point is on any opening
                        wall.openings.filter { opening ->
                            wall.openingPolygon(opening).contains(clickedPointInContent)
                        }.forEach { opening ->
                            selectedItems.add(opening)

                            // Check if the clicked point is on any item in the opening
                            opening.contents.filter { item ->
                                item.clickableArea(wall, opening).contains(clickedPointInContent)
                            }.forEach { item ->
                                selectedItems.add(item)
                            }
                        }
                    }
                }
                onUpdate?.invoke()

                for (item in selectedItems) {
                    logger.info("Selected item: $item")
                }

                renderPlan()
            }
        }

        pane.setOnMouseDragged { event ->
            if (!event.isPrimaryButtonDown) {
                return@setOnMouseDragged
            }

            val current = Point2D(event.x, event.y)
            val delta = current.subtract(lastMouseInPane)
            content.translateX += delta.x
            content.translateY += delta.y
            lastMouseInPane = current
            lastMouseInScene = Point2D(event.sceneX, event.sceneY)
            onUpdate?.invoke()
        }

        pane.setOnMouseMoved { event ->
            lastMouseInPane = Point2D(event.x, event.y)
            lastMouseInScene = Point2D(event.sceneX, event.sceneY)
            onUpdate?.invoke()
        }

        renderPlan()
    }

    fun renderOriginMarker() {
        val originMarkerGroup = Group()
        val lengthProperty = Bindings.divide(20.0, scale)
        val originMarkerV = Line(0.0, 0.0, 0.0, 0.0)
        originMarkerV.startYProperty().bind(Bindings.multiply(-1.0, lengthProperty))
        originMarkerV.endYProperty().bind(lengthProperty)
        val originMarkerH = Line(0.0, 0.0, 0.0, 0.0)
        originMarkerH.startXProperty().bind(Bindings.multiply(-1.0, lengthProperty))
        originMarkerH.endXProperty().bind(lengthProperty)
        listOf(originMarkerV, originMarkerH).forEach { line ->
            line.stroke = Color.RED
            line.strokeWidthProperty().bind(Bindings.divide(0.5, scale))
            originMarkerGroup.children.add(line)
        }

        content.children.add(originMarkerGroup)
    }

    fun renderPlan() {
        content.children.clear()
        renderOriginMarker()

        logger.debug("Rendering walls")
        val wallsShape = WallRenderer.renderWalls(plan.getWallsOnLevel(level))
        content.children.add(wallsShape)
        for (wall in plan.getWallsOnLevel(level)) {
            if (selectedItems.isNotEmpty() && selectedItems.last() == wall) {
                val highlightArea = getSelectionHighlight(wall.areaPolygon)
                content.children.add(highlightArea)
            }

            for (opening in wall.openings) {
                if (selectedItems.isNotEmpty() && selectedItems.last() == opening) {
                    val highlightArea = getSelectionHighlight(wall.openingPolygon(opening))
                    content.children.add(highlightArea)
                }

                for (item in opening.contents) {
                    logger.debug("Rendering item ${item.id} of type ${item::class.simpleName} in opening ${opening.id} on wall ${wall.id}")
                    val shape = item.render2D(wall, opening)
                    content.children.add(shape)
                    if (selectedItems.isNotEmpty() && selectedItems.last() == item) {
                        logger.info("Highlighting selected item ${item.id} of type ${item::class.simpleName} in opening ${opening.id} on wall ${wall.id}")
                        val highlightArea = getSelectionHighlight(item.clickableArea(wall, opening))
                        content.children.add(highlightArea)
                    }
                }
            }
        }
    }

    private fun getSelectionHighlight(highlightArea: Shape): Shape {
        highlightArea.fill = Color.color(0.0, 0.0, 0.0, 0.15)
        highlightArea.stroke = Color.GREY
        highlightArea.strokeWidth = 0.005
        return highlightArea
    }
}