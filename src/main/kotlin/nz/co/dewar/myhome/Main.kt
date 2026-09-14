package nz.co.dewar.myhome

import javafx.application.Application
import javafx.stage.Stage
import nz.co.dewar.myhome.gui.ApplicationContext
import nz.co.dewar.myhome.gui.MainWindow
import nz.co.dewar.myhome.model.Plan

class MyHomeApplication : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "MyHome"
        val mainWindow = MainWindow(primaryStage)

        val fileName = "C:\\Users\\George\\code\\myhome\\data\\McKeefry.json"
        ApplicationContext.plan = Plan.loadFromFile(fileName)
        ApplicationContext.level = 0

        mainWindow.planView2d.renderPlan()
        mainWindow.itemTree.populateTree()

        primaryStage.show()
    }
}

fun main() {
    Application.launch(MyHomeApplication::class.java)
}