package nz.co.dewar.myhome.gui.controls

import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox

data class RadioGroupItem(val value: String, val label: String)

class HorizontalRadioGroup(
    label: String,
    values: List<RadioGroupItem>,
    selectedValue: String,
    onChange: (String) -> Unit
) : HBox() {
    val toggleGroup = ToggleGroup()
    val hBox = HBox()

    init {
        hBox.children.add(Label(label))
        for (entry in values) {
            val radioButton = RadioButton(entry.label)
            radioButton.toggleGroup = toggleGroup
            if (selectedValue == entry.value) {
                radioButton.isSelected = true
            }
            radioButton.setOnAction {
                onChange(entry.value)
            }
            hBox.children.add(radioButton)
        }
    }
}