package nz.co.dewar.myhome

import javafx.application.Application
import javafx.scene.canvas.Canvas
import javafx.scene.shape.Shape
import javafx.stage.Stage
import kotlinx.serialization.json.Json
import nz.co.dewar.myhome.model.Plan
import nz.co.dewar.myhome.graphics2d.WallRenderer
import nz.co.dewar.myhome.gui.MainWindow
import java.io.File

class MyHomeApplication : Application() {
    lateinit var canvas: Canvas

    override fun start(primaryStage: Stage) {
        primaryStage.title = "MyHome"
//        val root = VBox()
//        val scene = Scene(root, 800.0, 600.0)
//
//        val menuBar = MenuBar()
//        val fileMenu = Menu("_File")
//        fileMenu.items.add(MenuItem("_Close").apply {
//            onAction = EventHandler { primaryStage.close() }
//        })
//        menuBar.menus.add(fileMenu)
//        root.children.add(menuBar)
//
//        canvas = Canvas(800.0, 600.0)
//        //root.children.add(canvas)
//        val walls = Group()
//        walls.children.add(renderPlan())
//
//        walls.setScaleX(1.0) // pixels per metre
//        walls.setScaleY(1.0)
//        walls.setTranslateX(0.0)
//        walls.setTranslateY(0.0)
//
//        root.children.add(walls)

        val mainWindow = MainWindow(primaryStage)
        primaryStage.show()
    }

    fun renderPlan(): Shape {
        val json = Json { ignoreUnknownKeys = true }
        val inputFile = File("C:\\Users\\George\\code\\myhome\\data\\McKeefry.json")
        val data = json.decodeFromString<Plan>(inputFile.readText())

        for(building in data.buildings) {
            println("Building: ${building.name}")
            println("  Floors:")
            for (floor in building.levels) {
                println("    Floor ${floor.number}: ${floor.name}")
                println("      Rooms:")
                for (room in floor.rooms) {
                    println("        Room: ${room.name}")
                }
            }
        }

        // Add the custom drawing panel
        val gc = canvas.graphicsContext2D
        val panel = WallRenderer(data.buildings[0])
        return panel.getWallsShape()
    }
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    try {
        Application.launch(MyHomeApplication::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}