package org.example.gui

import javafx.scene.control.Button
import javafx.scene.control.ToolBar

class MainToolbar {
    val toolBar = ToolBar()
    val levelUp = Button("^").apply { toolBar.items.add(this) }
    val levelDown = Button("v").apply { toolBar.items.add(this) }
}