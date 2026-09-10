package nz.co.dewar.myhome.gui

import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import nz.co.dewar.myhome.gui.ApplicationContext.plan

class ItemTreeView {
    private val treeRoot = TreeItem("Plan").apply { isExpanded = true }
    val treeView = TreeView(treeRoot)

    fun populateTree() {
        treeRoot.children.clear()
        for (building in plan.buildings) {
            val buildingItem = TreeItem(building.name).apply { isExpanded = true }
            treeRoot.children.add(buildingItem)

            for (level in building.levels) {
                val levelItem = TreeItem<String>(level.name).apply { isExpanded = true }
                buildingItem.children.add(levelItem)

                for (wall in level.walls) {
                    val wallItem = TreeItem(wall.id)
                    levelItem.children.add(wallItem)

                    for (opening in wall.openings) {
                        val openingItem = TreeItem(opening.id)
                        wallItem.children.add(openingItem)

                        for (item in opening.contents) {
                            val itemTreeItem = TreeItem(item.id ?: item::class.simpleName ?: "Unknown Item")
                            openingItem.children.add(itemTreeItem)
                        }
                    }
                }

                for (room in level.rooms) {
                    val roomItem = TreeItem(room.name).apply { isExpanded = true }
                    levelItem.children.add(roomItem)
                }
            }
        }
    }
}