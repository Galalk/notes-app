package service

import model.NoteItem
import persistence.Serializer

class NoteItemService(serializerType: Serializer) {

    private var noteItems = ArrayList<NoteItem>()

    fun addItemToNote(noteId: Int, itemId: Int): Boolean {

        if (noteItems.any {
                it.noteId == noteId && it.itemId == itemId
            }) {
            return false
        }
        noteItems.add(
            NoteItem(noteId, itemId)
        )
        return true
    }

    fun getItemIdsForNote(noteId: Int): List<Int> {
        return noteItems
            .filter { it.noteId == noteId }
            .map { it.itemId }
    }

}