package nz.co.dewar.myhome.gui

import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import nz.co.dewar.myhome.gui.ApplicationContext.plan

class MainToolbar {
    val toolBar = ToolBar()

    init {
        ApplicationContext.planChangeListeners.add { updateLevelButtons() }
        updateLevelButtons()
    }

    fun updateLevelButtons() {
        val levelNumbers = plan.buildings.flatMap { it.levels }.map { it.number }.sorted()
        for (levelNumber in levelNumbers) {
            val levelNames =
                plan.buildings.flatMap { it.levels }.filter { it.number == levelNumber }.map { it.name }.toSet()
            val levelName = if (levelNames.size == 1) levelNames.first() else "Level $levelNumber"
            val levelButton = Button(levelName)
            levelButton.setOnAction {
                ApplicationContext.level = levelNumber
            }
            toolBar.items.add(levelButton)
        }
    }
}