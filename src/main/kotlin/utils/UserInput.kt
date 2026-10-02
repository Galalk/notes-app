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

