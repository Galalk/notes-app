import com.sun.jndi.toolkit.dir.DirSearch.search
import io.github.oshai.kotlinlogging.KotlinLogging
import model.Item
import model.Note
import persistence.JSONSerializer
import persistence.XMLSerializer
import service.ItemService
import service.NoteItemService
import service.NoteService
import utils.readNextBoolean
import utils.readNextDouble
import utils.readNextInt
import utils.readNextLine
import utils.readValidCategory
import utils.readValidPriority
import utils.readValidStatus
import utils.readValidTags
import java.io.File

//val noteService = NoteService(XMLSerializer(File("notes.xml")))
val noteService = NoteService(JSONSerializer(File("notes.json")))
val itemService = ItemService(JSONSerializer(File("items.json")))
val noteItemService = NoteItemService(JSONSerializer(File("noteitems.json")))
private val logger = KotlinLogging.logger {}

fun main(){
    logger.info { "Notes App Started" }

    runMenu()
}

fun mainMenu(): Int {
    print(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |   5) Archive a note            |
         > |   6) Search note contents      |
         > |   7) Add item to a note        |
         > |   8) View items for a note     |
         > ----------------------------------
         > | ITEM MENU                      |
         > |   10) Add an item              |
         > |   11) List all items           |
         > |   12) Search item contents     |
         > |   13) .....                    |
         > ----------------------------------
         > | REPORTS MENU                   |
         > |   20) Items Report             |
         > |   21) ....                     |
         > ----------------------------------
         > |   90) Save notes               |
         > |   91) Load notes               |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > """.trimMargin(">"))
    return readNextInt("==>> ")
}

fun runMenu (){
    var input : Int
    do {
        input = mainMenu()
        when(input) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            5 -> archiveNote()
            6 -> searchByContent()
            7 -> addItemToNote()
            8 -> viewItemsForNote()
            10 -> addItem()
            11 -> listItems()
            12 -> searchItemDescription()
            20 -> itemReport()
            90 -> save()
            91 -> load()
            0 ->  {
                println("Exiting App")
                logger.info { "Notes App Exiting" }
            }
            else -> println("Invalid Option")
        }
    } while (input != 0)
}

fun addNote(){
    val title = readNextLine("Title: ")
    val body = readNextLine("Body: ")
    val priority = readValidPriority("Priority (1-5): ")
    val category = readValidCategory("Category \n(Home, College, Exercise, Cooking, Work, Leisure): ")
    val status = readValidStatus("Status (Todo, Doing, Done): ")
    val tags = readValidTags("Tags (Separate with commas [,]:")
    val estimatedTime = readNextDouble("Estimated time (Hours): ")

    noteService.addNote(
        Note(0, title, body, priority, category, false, status, tags, estimatedTime)
    )

    println("Note added")
}

fun listNotes() {
    if (noteService.numberOfNotes() > 0){
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > |   4) Filter by category      |
                  > |   5) Filter by priority      |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> noteService.getNotes().forEach {println(it)}
            2 -> noteService.getActiveNotes().forEach {println(it)}
            3 -> noteService.getArchivedNotes().forEach {println(it)}
            4 -> {
                val category = readNextLine("Enter category: ")
                val matchingNotes = noteService.getNotesByCategory(category)

                if (matchingNotes.isEmpty()) {
                    println("No notes found in category: $category")
                } else {
                    matchingNotes.forEach { println(it) }
                }
            }
            5 -> {
                val priority = readNextInt("Enter priority (1-5): ")
                val matchingNotes = noteService.getNotesByPriority(priority)

                if (matchingNotes.isEmpty()) {
                    println("No notes found with priority: $priority")
                } else {
                    matchingNotes.forEach { println(it) }
                }
            }

            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No notes stored")
    }

}

fun deleteNote() {
    print("Enter ID to delete: ")
    val id = readln().toInt()

    if (noteService.deleteNote(id)) {
        println("Deleted")
    } else {
        println("Not found")
    }
}

fun updateNote() {
    print("Enter ID to update: ")
    val id = readln().toInt()

    val title = readNextLine("Title: ")
    val body = readNextLine("Body: ")
    val priority = readNextInt("Priority (1-5): ")
    val category = readNextLine("Category: ")
    val isArchived = readNextBoolean("Is Archived (y/n)")
    val status = readValidStatus("Status (Todo, Doing, Done): ")
    val tags = readValidTags("Tags (Separate with commas [,]:")
    val estimatedTime = readNextDouble("Estimated Time: ")

    val updated = Note(id, title, body, priority, category, isArchived, status,tags, estimatedTime)

    if (noteService.updateNote(id, updated)) {
        println("Updated")
    } else {
        println("Not found")
    }
}

fun archiveNote(){
    noteService.getActiveNotes().forEach { println(it) }
    val id = readNextInt("Enter ID of Note to be Archived: ")
    if (noteService.archiveNote(id))
        println("Archived")
    else
        println("Not found")
}

fun searchByContent() {
    val content = readNextLine("Enter search contents: ")

    if (content != "") {
        val results = noteService.searchNotesByContent(content)

        if (results.isNotEmpty()) {
            results.forEach { println(it) }
        } else {
            println("No notes found")
        }
    }
}

fun addItem() {
    val description = readNextLine("Description: ")
    val isCompleted = readNextBoolean("Completed (y/n): ")

    itemService.addItem(
        Item(0, description, isCompleted)
    )
    println("Item added")
}

fun listItems() {
    if (itemService.getItems().isEmpty()) {
        println("No items stored")
    } else {
        itemService.getItems().forEach { println(it) }
    }
}

fun searchItemDescription() {
    val content = readNextLine("Enter search contents: ")
    if (content != "") {
        itemService.searchItemsByDescription(content).forEach { println(it) }
    }
}

fun itemReport(){
    println("""
        | ----------------------
        | ITEM REPORT
        | ----------------------
        | Total Items:      ${itemService.numberOfItems()}
        | Completed Items:  ${itemService.numberOfCompletedItems()}
        | Incomplete Items: ${itemService.numberOfIncompleteItems()}
        | ----------------------        
    """.trimIndent())
}

fun addItemToNote() {
    val noteId = readNextInt("Enter Note ID: ")
    if (noteService.findNoteById(noteId) != null) {
        val itemId = readNextInt("Enter Item ID: ")
        if (itemService.findItemById(itemId) != null) {
            if (noteItemService.addItemToNote(noteId, itemId)){
                println("Added")
            }
            else{
                println("Item is already added to Note")
            }
        }
        else{
            println("Item not found")
        }
    } else {
        println("Note not found")
    }
}

fun viewItemsForNote() {
    val id = readNextInt("Enter Note ID: ")
    val itemsIds = noteItemService.getItemIdsForNote(id)
    if (itemsIds.isNotEmpty()) {
        itemsIds.forEach { itemId -> println(itemService.findItemById(itemId))}
    }
}

fun save() {
    try {
        noteService.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteService.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}




