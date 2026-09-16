package nz.co.dewar.myhome.gui.dialog

import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import nz.co.dewar.myhome.gui.ApplicationContext
import nz.co.dewar.myhome.gui.MainWindow
import nz.co.dewar.myhome.model.Room

class RoomPropertiesDialog(val mainWindow: MainWindow, val room: Room) {
    val root = VBox(8.0).apply { padding = Insets(8.0) }
    val scene = Scene(root, 640.0, 480.0)
    val stage = Stage()

    init {
        stage.title = "Room Properties"
        stage.isResizable = false
        stage.initOwner(mainWindow.stage)
        stage.scene = scene

        root.children.add(Label("Editing Plan -> ${room.level.building.name} -> ${room.level.name} -> ${room.name}"))

        val roomNameGroup = HBox()
        roomNameGroup.children.add(Label("Room Name:").apply { prefWidth = 100.0 })
        val roomNameField = TextField(room.name)
        roomNameGroup.children.add(roomNameField)
        root.children.add(roomNameGroup)

        val saveButton = Button("Save").apply {
            onAction = {
                val newName = roomNameField.text
                if (newName.isNotBlank()) {
                    room.name = newName
                    ApplicationContext.planChanged()
                    stage.close()
                }
            }
        }
        root.children.add(saveButton)

        stage.show()
    }
}