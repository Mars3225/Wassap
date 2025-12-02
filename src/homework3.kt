fun calculator() {
    val input = readLine() ?: return

    val operator = when {
        input.contains("+") || input.contains(" + ") -> "+"
        input.contains("-") || input.contains(" - ") -> "-"
        input.contains("*") || input.contains(" * ") -> "*"
        input.contains("/") || input.contains(" / ") -> "/"
        else -> {
            println("Неизвестная операция")
            return
        }
    }

    val parts = input.split(operator)
    if (parts.size != 2) {
        println("Соре, я не такой умный")
        return
    }

    val first = parts[0].trim().toInt()
    val second = parts[1].trim().toInt()

    val result = when (operator) {
        "+" -> first + second
        "-" -> first - second
        "*" -> first * second
        "/" -> {
            if (second == 0) {
                println("Какое тебе деление на 0, паскуда?")
                return
            }
            first / second
        }

        else -> 0
    }

    println("Результат: $result")
}

fun masivi() {
    val users = arrayOf("Valera", "Max1k")
    val ages = arrayOf(12, 52)

    for (i in users.indices) {
        println("${users[i]} ${ages[i]}")
    }
}

fun main() {
    /*while (true) {
        calculator()
    }*/
    masivi()
}
