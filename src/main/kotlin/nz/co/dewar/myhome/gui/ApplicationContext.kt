package nz.co.dewar.myhome.gui

import nz.co.dewar.myhome.model.Plan
import nz.co.dewar.myhome.model.SelectableItem

object ApplicationContext {
    var plan: Plan = Plan(listOf())
        set(value) {
            field = value
            planChanged()
        }
    var level: Int = 0
        set(value) {
            field = value
            levelChanged()
        }

    /** All selected items; the last item is the active selection, the others are other items matching the clicked position */
    var selectedItems: MutableList<SelectableItem> = mutableListOf()
        set(value) {
            field = value
            selectionChanged()
        }

    fun planChanged() {
        planChangeListeners.forEach { it() }
    }

    fun levelChanged() {
        levelChangeListeners.forEach { it() }
    }

    fun selectionChanged() {
        selectionChangeListeners.forEach { it() }
    }

    // Collections of listeners for changes in application context properties
    var planChangeListeners = mutableListOf<() -> Unit>()
    var levelChangeListeners = mutableListOf<() -> Unit>()
    var selectionChangeListeners = mutableListOf<() -> Unit>()
}