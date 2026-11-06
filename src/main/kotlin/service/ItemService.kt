package service

import model.Item
import persistence.Serializer

class ItemService(serializerType: Serializer) {

    private var items = ArrayList<Item>()

    private var lastId = 0
    private fun getId(): Int = lastId++

    fun addItem(item: Item) {
        item.id = getId()
        items.add(item)
    }

    fun getItems(): List<Item> {
        return items
    }

    fun findItemById(id: Int): Item? {
        return items.find { it.id == id }
    }

    fun searchItemsByDescription(content: String) = items
        .filter { item -> item.description.contains(
            content, ignoreCase = true) }

    fun numberOfItems() = items.size
    fun numberOfCompletedItems() = items.count { it.isCompleted}
    fun numberOfIncompleteItems() = items.count { !it.isCompleted}
}

