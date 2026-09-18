package nz.co.dewar.myhome.graphics3d

import javafx.application.Platform
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.PerspectiveCamera
import javafx.scene.SubScene
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.shape.Box
import javafx.scene.shape.DrawMode
import javafx.scene.transform.Rotate
import nz.co.dewar.myhome.gui.ApplicationContext
import nz.co.dewar.myhome.gui.ApplicationContext.level
import nz.co.dewar.myhome.gui.ApplicationContext.plan
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class CameraState(
    var posX: Double = 0.0,
    var posY: Double = 0.0,
    var posZ: Double = -100.0,
    var angleX: Double = 0.0,
    var angleY: Double = 0.0,
    var angleZ: Double = 0.0
)

class Renderer3D {
    val logger: Logger = LoggerFactory.getLogger(Renderer3D::class.java)
    val root = Group()
    val subScene = SubScene(root, 600.0, 300.0)
    val camera = PerspectiveCamera(true)
    val cameraRotateX = Rotate(0.0, Rotate.X_AXIS)
    val cameraRotateY = Rotate(0.0, Rotate.Y_AXIS)
    val cameraRotateZ = Rotate(0.0, Rotate.Z_AXIS)
    val cameraState = CameraState()

    private var lastMouseInPane = Point2D(0.0, 0.0)

    init {
        subScene.isFocusTraversable = true
        subScene.fill = Color.GREEN
        root.style = "-fx-background-color: lightblue;"

        val box = Box(5.0, 2.4, 0.1)
        box.translateX = 100.0
        box.translateY = 50.0
        box.drawMode = DrawMode.FILL
        root.children.add(box)

        camera.fieldOfView = 90.0
        camera.nearClip = 0.1
        camera.farClip = 5000.0
        camera.transforms.addAll(cameraRotateX, cameraRotateY, cameraRotateZ)
        updateCamera()

        subScene.camera = camera

        subScene.onMousePressed = {
            logger.info("Mouse pressed at: (${it.sceneX}, ${it.sceneY})")
            Platform.runLater { subScene.requestFocus() }
        }

        subScene.setOnMouseDragged { event ->
            if (!event.isPrimaryButtonDown) {
                return@setOnMouseDragged
            }

            val current = Point2D(event.screenX, event.screenY)
            if (lastMouseInPane.x == 0.0 && lastMouseInPane.y == 0.0) {
                lastMouseInPane = current
                return@setOnMouseDragged
            }
            val delta = current.subtract(lastMouseInPane)
            lastMouseInPane = current
            cameraState.angleY -= delta.x * 0.5
            cameraState.angleX += delta.y * 0.5
            updateCamera()
            logger.info("Mouse dragged. Delta: (${delta.x}, ${delta.y}). Camera rotate: (${cameraRotateX.angle}, ${cameraRotateY.angle}, ${cameraRotateZ.angle})")
        }

        subScene.onMouseReleased = {
            lastMouseInPane = Point2D(0.0, 0.0)
        }

        subScene.onKeyPressed = {
            logger.info("Key pressed: ${it.code}")
            when (it.code) {
                KeyCode.W -> cameraState.posZ += 10
                KeyCode.S -> cameraState.posZ -= 10
                KeyCode.A -> cameraState.posX -= 10
                KeyCode.D -> cameraState.posX += 10
                KeyCode.Z -> cameraState.posY += 10
                KeyCode.X -> cameraState.posY -= 10
//                KeyCode.I -> cameraState.angleZ += 10
//                KeyCode.K -> cameraState.angleZ -= 10
                KeyCode.J -> cameraState.angleY -= 10
                KeyCode.L -> cameraState.angleY += 10
                KeyCode.I -> cameraState.angleX += 10
                KeyCode.K -> cameraState.angleX -= 10
                else -> logger.info("Unhandled key: ${it.code}")
            }
            updateCamera()

        }
    }

    fun renderPlan() {
        root.children.clear()
        for (wall in plan.getWallsOnLevel(level)) {
            val wall3D = Box(wall.vector.length.metres, 2.4, wall.thickness.value.metres)
            wall3D.translateX = wall.centerLine.start.x
            wall3D.translateZ = wall.centerLine.start.y
            wall3D.drawMode = DrawMode.FILL
            logger.debug("Adding wall to 3D scene: ${wall.id}, start: ${wall.centerLine.start}, end: ${wall.centerLine.end}, length: ${wall.vector.length.metres}, thickness: ${wall.thickness.value.metres}")
            root.children.add(wall3D)
        }
    }

    fun updateCamera() {
        camera.translateX = cameraState.posX
        camera.translateY = cameraState.posY
        camera.translateZ = cameraState.posZ
        cameraRotateX.angle = cameraState.angleX
        cameraRotateY.angle = cameraState.angleY
        cameraRotateZ.angle = cameraState.angleZ
        ApplicationContext.cameraMoved(cameraState)
    }
}