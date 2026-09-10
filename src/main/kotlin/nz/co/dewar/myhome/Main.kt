package nz.co.dewar.myhome

import javafx.application.Application
import javafx.stage.Stage
import kotlinx.serialization.json.Json
import nz.co.dewar.myhome.gui.ApplicationContext
import nz.co.dewar.myhome.gui.MainWindow
import nz.co.dewar.myhome.model.Plan
import java.io.File

class MyHomeApplication : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "MyHome"
        val mainWindow = MainWindow(primaryStage)

        val json = Json { ignoreUnknownKeys = true }
        val inputFile = File("C:\\Users\\George\\code\\myhome\\data\\McKeefry.json")
        ApplicationContext.plan = json.decodeFromString<Plan>(inputFile.readText())
        ApplicationContext.level = 0

        mainWindow.planView2d.renderPlan()

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