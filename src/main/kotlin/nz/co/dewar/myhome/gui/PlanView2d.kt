package nz.co.dewar.myhome.gui

import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.MouseButton
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import kotlinx.serialization.json.Json
import nz.co.dewar.myhome.model.Plan
import nz.co.dewar.myhome.graphics2d.WallRenderer
import nz.co.dewar.myhome.gui.ApplicationContext.level
import nz.co.dewar.myhome.gui.ApplicationContext.plan
import nz.co.dewar.myhome.model.SelectableItem
import java.io.File


class PlanView2d {
    val pane = Pane()

    var onUpdate: (() -> Unit)? = null

    private val content = Group()
    var scale = 20.0
    private var lastMouseInPane = Point2D(0.0, 0.0)
    private var lastMouseInScene = Point2D(0.0, 0.0)

    /** All selected items; the last item is the active selection, the others are other items matching the clicked position */
    var selectedItems: MutableList<SelectableItem> = mutableListOf()

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
        pane.background = Background(BackgroundFill(Color.LIGHTGRAY, null, null))

        // Set initial scale
        content.scaleX = scale
        content.scaleY = scale
        content.translateX = 200.0
        content.translateY = 200.0

        val circle = Circle(0.0, 0.0, 1.0, Color.BLUE)
        content.children.add(circle)
        pane.children.add(content)

        pane.setOnScroll { event ->
            val zoomFactor = if (event.deltaY > 0) 1.1 else 1.0 / 1.1
            val oldScale = scale
            val newScale = (oldScale * zoomFactor).coerceIn(20.0, 200.0)
            if (newScale == oldScale) {
                return@setOnScroll
            }

            val pivotInContent = content.sceneToLocal(event.sceneX, event.sceneY)

            scale = newScale
            content.scaleX = scale
            content.scaleY = scale

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
                    println("Selected item: $item")
                }
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

    fun renderPlan() {
        content.children.clear()

        val wallsShape = WallRenderer.renderWalls(plan.getWallsOnLevel(level))
        content.children.add(wallsShape)
        for (wall in plan.getWallsOnLevel(level)) {
            for (opening in wall.openings) {
                for (item in opening.contents) {
                    println("Rendering item ${item.id} of type ${item::class.simpleName} in opening ${opening.id} on wall ${wall.id}")
                    val shape = item.render2D(wall, opening)
                    content.children.add(shape)
                }
            }
        }
    }
}