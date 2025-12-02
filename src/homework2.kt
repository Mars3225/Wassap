fun main() {
    time()
    temp()
}

fun time() {
    print("Введите значение времени в формате ЧЧ:ММ: ")
    val input = readLine() ?: return

    val parts = input.split(":")
    if (parts.size != 2) {
        println("Неверный формат! Используйте ЧЧ:ММ")
        return
    }

    val hour = parts[0].toInt()
    val minute = parts[1].toInt()

    if (hour !in 0..23 || minute !in 0..59) {
        println("Неверное время!")
        return
    }

    when (hour) {
        in 6..11 -> {
            println("Доброе утро!")
        }

        in 12..17 -> {
            println("Добрый день!")
        }

        in 18..23 -> {
            println("Добрый вечер!")
        }

        else -> {
            println("Доброй ночи!")
        }
    }

    when (input) {
        "9:00" -> println("Проснуться")
        "09:00" -> println("Проснуться")
        "10:00" -> println("Поехать на работу")
    }
}

fun temp() {
    print("Введите значение температуры: ")
    val a = readLine()
    val b: Int = a?.toInt() ?: 0
    val c = "На улице"
    val clothes = Array(5) { "" }

    clothes[0] = "Оденься теплее."
    clothes[1] = "Возьми с собой кофту."
    clothes[2] = "В футболке норм."
    clothes[3] = "Под кондиционером жить можно."
    clothes[4] = "Лучше не выходить."

    when (b) {
        in 0..6 -> println("$c холодно. " + clothes[0])
        in 7..13 -> println("$c ну вроде норм. " + clothes[1])
        in 14..22 -> println("$c тепло. " + clothes[2])
    }
    if (b > 22) {
        println("$c жарко. " + clothes[3])
    } else if (b < 0) {
        println("$c ебняк. " + clothes[4])
    }
}
