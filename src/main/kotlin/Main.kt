import io.github.oshai.kotlinlogging.KotlinLogging
import model.Note
import persistence.JSONSerializer
import persistence.XMLSerializer
import service.NoteService
import utils.readNextBoolean
import utils.readNextInt
import utils.readNextLine
import java.io.File

//val noteService = NoteService(XMLSerializer(File("notes.xml")))
val noteService = NoteService(JSONSerializer(File("notes.json")))

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
         > ----------------------------------
         > |   20) Save                     |
         > |   21) Load                     |
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
            20 -> save()
            21 -> load()
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
    val priority = readNextInt("Priority (1-5): ")
    val category = readNextLine("Category: ")
    val isArchived = readNextBoolean("Is Archived (y/n)")

    noteService.addNote(
        Note(0, title, body, priority, category, isArchived)
    )

    println("Note added")
}

fun listNotes() = noteService.getNotes().forEach { println(it) }

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

    val updated = Note(id, title, body, priority, category, isArchived)

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




