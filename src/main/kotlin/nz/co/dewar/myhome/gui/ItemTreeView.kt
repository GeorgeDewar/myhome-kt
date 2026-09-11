package nz.co.dewar.myhome.gui

import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import nz.co.dewar.myhome.gui.ApplicationContext.plan
import nz.co.dewar.myhome.model.PlanItem
import nz.co.dewar.myhome.model.SelectableItem
import org.slf4j.LoggerFactory

class ItemTreeView {
    private val logger = LoggerFactory.getLogger(ItemTreeView::class.java)
    private val treeRoot = TreeItem<PlanItem>(plan).apply { isExpanded = true }
    val treeView = TreeView(treeRoot)

    init {
        treeView.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            if (newValue != null) {
                val selectedItem = newValue.value
                if (selectedItem is SelectableItem) {
                    logger.debug("Tree item selected: ${newValue.value}")
                    ApplicationContext.selectedItems = mutableListOf(selectedItem)
                }
            }
        }
    }

    fun populateTree() {
        logger.info("Populating item tree view with plan data")
        treeRoot.children.clear()
        for (building in plan.buildings) {
            val buildingItem = TreeItem<PlanItem>(building).apply { isExpanded = true }
            treeRoot.children.add(buildingItem)

            for (level in building.levels) {
                val levelItem = TreeItem<PlanItem>(level).apply { isExpanded = true }
                buildingItem.children.add(levelItem)

                for (wall in level.walls) {
                    val wallItem = TreeItem<PlanItem>(wall)
                    levelItem.children.add(wallItem)

                    for (opening in wall.openings) {
                        val openingItem = TreeItem<PlanItem>(opening)
                        wallItem.children.add(openingItem)

                        for (item in opening.contents) {
                            val itemTreeItem = TreeItem<PlanItem>(item)
                            openingItem.children.add(itemTreeItem)
                        }
                    }
                }

                for (room in level.rooms) {
                    val roomItem = TreeItem<PlanItem>(room).apply { isExpanded = true }
                    levelItem.children.add(roomItem)
                }
            }
        }
    }
}