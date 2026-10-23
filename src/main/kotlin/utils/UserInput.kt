package utils

fun readNextInt(prompt: String = ""): Int {
    while (true) {
        print(prompt)

        val value = readln().trim().toIntOrNull()
        if (value != null) {
            return value
        }

        println("Please enter a valid integer.")
    }
}

fun readNextDouble(prompt: String = ""): Double {
    while (true) {
        print(prompt)

        val valueDouble = readln().trim().toDoubleOrNull()
        if (valueDouble != null) {
            return valueDouble
        }

        println("Please enter a valid Double.")
    }
}

fun readNextFloat(prompt: String = ""): Float {
    while (true) {
        print(prompt)

        val valueFloat = readln().trim().toFloatOrNull()
        if (valueFloat != null) {
            return valueFloat
        }

        println("Please enter a valid Float.")
    }
}

fun readNextLine(prompt: String = ""): String {
    print(prompt)
    return readln()
}

fun readNextChar(prompt: String = ""): Char {
    while (true) {
        print(prompt)
        val input = readln().trim()

        if (input.length == 1) {
            return input[0]
        }

        println("Please enter exactly one character.")
    }
}

fun readNextBoolean(prompt: String = ""): Boolean {
    print(prompt)
    return when (readlnOrNull()?.lowercase()) {
        "y", "yes", "yeah", "yea", "true" -> true
        else -> false
    }
}


val categories = setOf("Home", "College", "Exercise", "Cooking", "Work", "Leisure")

fun isValidCategory(categoryToCheck: String?): Boolean {
    return categories.any {
        it.equals(categoryToCheck, ignoreCase = true)
    }
}

fun validRange(numberToCheck: Int, min: Int, max: Int): Boolean {
    return numberToCheck in min..max
}

fun readValidCategory(prompt: String): String {
    var input = readNextLine(prompt)
    do {
        if (isValidCategory(input))
            return input
        else {
            print("Invalid category $input.  Please try again: ")
            input = readNextLine(prompt)
        }
    } while (true)
}

fun readValidPriority(prompt: String): Int {
    var input = readNextInt(prompt)
    do {
        if (validRange(input, 1, 5))
            return input
        else {
            print("Invalid priority $input.")
            input = readNextInt(prompt)
        }
    } while (true)
}

fun readValidStatus(prompt: String): String {
    var input = readNextLine(prompt)
    do {
        if (isValidStatus(input))
            return input
        else {
            print("Invalid status $input.  Please try again: ")
            input = readNextLine(prompt)
        }
    } while (true)
}

fun readValidTags(prompt: String): List<String> {
    while (true) {
        val input = readNextLine(prompt)

        val tags = input.split(",")
            .map { it.trim() }

        if (areValidTags(tags)) {
            return tags
        } else {
            println("Tags cannot be empty. Please try again.")
        }
    }
}

val status = setOf("Todo", "Doing", "Done")

fun isValidStatus(statusToCheck: String?): Boolean {
    return status.any {
        it.equals(statusToCheck, ignoreCase = true)
    }
}

fun areValidTags(tags: List<String>): Boolean =
    tags.isNotEmpty() && tags.all { it.isNotBlank() }




