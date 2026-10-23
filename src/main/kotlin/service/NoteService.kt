package service

import model.Note
import persistence.Serializer

class NoteService(serializerType: Serializer) {

    private var serializer: Serializer = serializerType

    private var notes = ArrayList<Note>()
    private var lastId = 0
    private fun getId(): Int = lastId++

    fun addNote(note: Note) {
        note.id = getId()
        note.isArchived = false
        notes.add(note)
    }

    fun getNotes(): List<Note> {
        return notes
    }

    fun deleteNote(id: Int): Boolean {
        return notes.removeIf { it.id == id }
    }

    fun updateNote(id: Int, updated: Note): Boolean {
        val index = notes.indexOfFirst { it.id == id }
        if (index != -1) {
            updated.id = id
            notes[index] = updated
            return true
        }
        return false
    }

    fun findNoteById(id: Int): Note? {
        return notes.find { it.id == id }
    }

    fun searchNotesByContent(content: String) = notes
        .filter { note -> ((note.title.contains(content, ignoreCase = true)
                || (note.body.contains(content, ignoreCase = true))))}

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun numberOfArchivedNotes(): Int {
      return notes.stream()
          .filter {note: Note -> note.isArchived}
          .count()
          .toInt()
    }

    fun numberOfActiveNotes(): Int {
        return notes.stream()
            .filter { note: Note -> !note.isArchived }
            .count()
            .toInt()
    }

    fun numberOfNotesByCategory(category: String): Int {
        return notes.stream()
            .filter { note: Note -> note.category == category }
            .count()
            .toInt()
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        return notes.stream()
            .filter {note: Note -> note.priority == priority}
            .count()
            .toInt()
    }

    fun archiveNote(id: Int): Boolean {
        for (note in notes) {
            if (note.id == id && !note.isArchived) {
                note.isArchived = true
                return true
            }
        }
        return false
    }

    fun getActiveNotes(): List<Note> =
        notes.stream()
            .filter { !it.isArchived }
            .toList()

    fun getNotesByCategory(category: String): List<Note> =
        notes.stream()
            .filter { it.category.equals(category, ignoreCase = true) }
            .toList()

    fun getArchivedNotes(): List<Note> =
        notes.stream()
            .filter { it.isArchived }
            .toList()


    fun getNotesByPriority(priority: Int): List<Note> =
        notes.stream()
            .filter { it.priority == priority}
            .toList()


    fun load() {
        val array = serializer.read(Array<Note>::class.java)
        notes = array.toCollection(ArrayList())
        lastId = (notes.maxOfOrNull { it.id } ?: -1) + 1
    }

    //@Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

}





