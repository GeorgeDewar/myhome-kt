package nz.co.dewar.myhome.gui

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlin.math.roundToInt

class MainWindow(private val primaryStage: Stage) {
    val root = BorderPane()
    val planView2d = PlanView2d()

    init {
        val scene = Scene(root, 800.0, 600.0)

        val menuBar = MenuBar()
        val fileMenu = Menu("_File")
        fileMenu.items.add(MenuItem("_Close").apply {
            onAction = EventHandler { primaryStage.close() }
        })
        menuBar.menus.add(fileMenu)
        val mainToolbar = MainToolbar()
        val topContainer = VBox()
        topContainer.children.addAll(menuBar, mainToolbar.toolBar)
        root.top = topContainer

        root.center = planView2d.pane

        val statusBar = StatusBar()
        planView2d.onUpdate = {
            statusBar.cursorPositionLabel.text = "Cursor: (${(planView2d.cursorPositionWorld.x * 1000).roundToInt()}mm, ${(planView2d.cursorPositionWorld.y * 1000).roundToInt()}mm)"
            statusBar.scaleLabel.text = "Scale: ${planView2d.scale}"
            statusBar.offsetLabel.text = "Offset: (${(planView2d.offset.x * 1000).roundToInt()}mm, ${(planView2d.offset.y * 1000).roundToInt()}mm)"
        }
        root.bottom = statusBar.root

        primaryStage.scene = scene
    }
}