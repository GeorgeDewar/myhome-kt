package nz.co.dewar.myhome.gui

import nz.co.dewar.myhome.model.Plan
import nz.co.dewar.myhome.model.SelectableItem

object ApplicationContext {
    var plan: Plan = Plan(listOf())
    var level: Int = 0

    /** All selected items; the last item is the active selection, the others are other items matching the clicked position */
    var selectedItems: MutableList<SelectableItem> = mutableListOf()
}