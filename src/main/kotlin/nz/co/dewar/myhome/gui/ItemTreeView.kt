package nz.co.dewar.myhome.gui

import javafx.collections.ListChangeListener
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeCell
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
        treeView.selectionModel.selectionMode = SelectionMode.MULTIPLE
        treeView.selectionModel.selectedItems.addListener(ListChangeListener<TreeItem<PlanItem>> {
            val selectedItems = treeView.selectionModel.selectedItems.mapNotNull { it.value as? SelectableItem }
            ApplicationContext.selectedItems = selectedItems.toMutableList()
        })
        treeView.cellFactory = {
            object : TreeCell<PlanItem>() {
                override fun updateItem(item: PlanItem?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = item?.treeLabel
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

                val wallsItem = TreeItem<PlanItem>(object : PlanItem {
                    override val treeLabel = "Walls"
                }).apply { isExpanded = true }
                levelItem.children.add(wallsItem)
                for (wall in level.walls) {
                    val wallItem = TreeItem<PlanItem>(wall)
                    wallsItem.children.add(wallItem)

                    for (opening in wall.openings) {
                        val openingItem = TreeItem<PlanItem>(opening)
                        wallItem.children.add(openingItem)

                        for (item in opening.contents) {
                            val itemTreeItem = TreeItem<PlanItem>(item)
                            openingItem.children.add(itemTreeItem)
                        }
                    }
                }

                val roomsItem = TreeItem<PlanItem>(object : PlanItem {
                    override val treeLabel = "Rooms"
                }).apply { isExpanded = true }
                levelItem.children.add(roomsItem)
                for (room in level.rooms) {
                    val roomItem = TreeItem<PlanItem>(room).apply { isExpanded = true }
                    roomsItem.children.add(roomItem)
                }
            }
        }
    }
}