package nz.co.dewar.myhome.graphics3d

import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.paint.Color

class Renderer3D {
    val root = Group()

    init {
        val scene = Scene(root, 1024.0, 768.0, true)
        scene.fill = Color.GREY
    }
}