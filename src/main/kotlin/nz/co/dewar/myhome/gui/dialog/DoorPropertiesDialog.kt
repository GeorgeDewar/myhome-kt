package nz.co.dewar.myhome.gui.dialog

import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import nz.co.dewar.myhome.gui.ApplicationContext
import nz.co.dewar.myhome.gui.MainWindow
import nz.co.dewar.myhome.gui.controls.HorizontalRadioGroup
import nz.co.dewar.myhome.gui.controls.RadioGroupItem
import nz.co.dewar.myhome.model.openingitem.HingeSide
import nz.co.dewar.myhome.model.openingitem.OpeningItemType
import nz.co.dewar.myhome.model.openingitem.StandardDoor
import nz.co.dewar.myhome.model.openingitem.SwingDirection

class DoorPropertiesDialog(val mainWindow: MainWindow, val door: StandardDoor) {
    val root = VBox(8.0).apply { padding = Insets(8.0) }
    val scene = Scene(root, 640.0, 480.0)
    val stage = Stage()

    init {
        stage.title = "Door Properties"
        stage.isResizable = false
        stage.initOwner(mainWindow.stage)
        stage.scene = scene

        root.children.add(Label("Editing Plan -> ${door.opening.wall.level.building.name} -> ${door.opening.wall.level.name} -> ${door.opening.wall.id} -> ${door.opening.id} -> ${door.id ?: "Unnamed door"}"))

        val openingTypeGroup = HBox()
        openingTypeGroup.children.add(Label("Opening Type:").apply { prefWidth = 100.0 })
        val openingTypeDropdown = ComboBox<OpeningItemType>()
        openingTypeDropdown.items.addAll(OpeningItemType.entries)
        openingTypeDropdown.value = OpeningItemType.entries.find { it.clazz == door::class }
        openingTypeGroup.children.add(openingTypeDropdown)
        root.children.add(openingTypeGroup)

        val hingeSideRadio = HorizontalRadioGroup(
            label = "Hinge Side:",
            values = HingeSide.entries.map { RadioGroupItem(it.name, it.label) },
            selectedValue = door.hingeSide.name,
            onChange = { newValue ->
                door.hingeSide = HingeSide.valueOf(newValue)
                ApplicationContext.planChanged()
            }
        )
        root.children.add(hingeSideRadio.hBox)

        val swingDirectionRadio = HorizontalRadioGroup(
            label = "Swing Direction:",
            values = SwingDirection.entries.map { RadioGroupItem(it.name, it.label) },
            selectedValue = door.swingDirection.name,
            onChange = { newValue ->
                door.swingDirection = SwingDirection.valueOf(newValue)
                ApplicationContext.planChanged()
            }
        )
        root.children.add(swingDirectionRadio.hBox)

        stage.show()
    }
}