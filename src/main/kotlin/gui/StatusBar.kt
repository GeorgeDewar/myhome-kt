package org.example.gui

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.layout.HBox

class StatusBar {
    val root = HBox()

    val cursorPositionLabel = Label()
    val scaleLabel = Label()
    val offsetLabel = Label()

    init {
        root.padding = Insets(6.0, 10.0, 6.0, 10.0)
        root.spacing = 4.0
        root.style = "-fx-background-color: #f0f0f0; -fx-border-color: #d3d3d3; -fx-border-width: 1 0 0 0;";

        val hello = Label("Hello, World!")
        root.children.add(hello)
        root.children.add(Separator(Orientation.VERTICAL))
        root.children.add(cursorPositionLabel)
        root.children.add(Separator(Orientation.VERTICAL))
        root.children.add(scaleLabel)
        root.children.add(Separator(Orientation.VERTICAL))
        root.children.add(offsetLabel)
    }

}