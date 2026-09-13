package nz.co.dewar.myhome.gui.dialog

import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage
import nz.co.dewar.myhome.gui.MainWindow
import nz.co.dewar.myhome.model.openingitem.StandardDoor

class DoorPropertiesDialog(val mainWindow: MainWindow, val door: StandardDoor) {
    val root = VBox()
    val scene = Scene(root, 640.0, 480.0)
    val stage = Stage()

    init {
        stage.title = "Door Properties"
        stage.isResizable = false
        stage.initOwner(mainWindow.stage)
        stage.scene = scene
        stage.show()
    }
}