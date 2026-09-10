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
    override fun start(primaryStage: Stage) {
        primaryStage.title = "MyHome"
        val mainWindow = MainWindow(primaryStage)
        primaryStage.show()
    }
}

fun main() {
    try {
        Application.launch(MyHomeApplication::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}