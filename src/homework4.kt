fun main() {
    val customer = registerCustomer()
    val courier = Courier("Данияр", speedKmPerMin = 1)

    val menu = listOf(
        Pizza("Пепперони", 1500.0),
        Burger("Чизбургер", 900.0),
        Sushi("Филадельфия", 2000.0)
    )

    println("Меню:")
    menu.forEachIndexed { index, food ->
        println("${index + 1}. ${food.name} (${food.price})")
    }

    var selectedFood: Food

    while (true) {
        val index = readPositiveInt("Введите номер блюда: ") - 1
        if (index in menu.indices) {
            selectedFood = menu[index]
            break
        }
        println("Такого блюда нет")
    }

    val distance = readPositiveInt("Введите расстояние доставки (км): ")

    customer.orderFood(selectedFood, courier, distance)?.process()
}

// Еда
abstract class Food(
    val name: String,
    val price: Double
) {
    abstract fun cook()
}

class Pizza(name: String, price: Double) : Food(name, price) {
    override fun cook() {
        println("Пицца $name готовится в печи 10 минут")
    }
}

class Burger(name: String, price: Double) : Food(name, price) {
    override fun cook() {
        println("$name жарится на гриле 5 минут")
    }
}

class Sushi(name: String, price: Double) : Food(name, price) {
    override fun cook() {
        println("Суши $name аккуратно крутятся поваром 7 минут")
    }
}

// Доставка
interface Delivery {
    fun calculateDeliveryTime(distance: Int): Int
    fun deliver(order: Order)
}

class Courier(
    val name: String,
    val speedKmPerMin: Int
) : Delivery {

    override fun calculateDeliveryTime(distance: Int): Int {
        val time = distance / speedKmPerMin
        println("Курьер $name оценивает время доставки на расстояние $distance км как ${formatMinutes(time)}")
        return time
    }

    override fun deliver(order: Order) {
        val time = calculateDeliveryTime(order.distance)
        println("Курьер $name доставляет заказ ${order.food.name} покупателю ${order.customer.name}. Время в пути: ${formatMinutes(time)}")
    }
}

// Покупатели
open class Customer(
    val name: String
) {
    protected var balance: Double = 0.0
        private set

    fun topUp(amount: Double) {
        balance += amount
        println("$name пополнил баланс на $amount. Баланс: $balance")
    }

    open fun pay(amount: Double): Boolean {
        return if (balance >= amount) {
            balance -= amount
            println("$name оплатил $amount. Остаток: $balance")
            true
        } else {
            false
        }
    }

    fun payWithAutoTopUp(amount: Double): Boolean {
        if (pay(amount)) return true

        val few = amount - balance
        println("Недостаточно средств. Не хватает: $few")

        print("Хотите пополнить баланс? (да / нет): ")
        if (readLine()?.lowercase() != "да") {
            println("Оплата отменена")
            return false
        }

        while (balance < amount) {
            val add = readPositiveDouble("Введите сумму пополнения: ")
            topUp(add)
        }

        return pay(amount)
    }

    fun orderFood(
        food: Food,
        courier: Courier,
        distance: Int
    ): Order? {
        println("\n$name хочет заказать: ${food.name} за ${food.price}")

        if (!payWithAutoTopUp(food.price)) return null

        println("Заказ создан.")
        return Order(this, courier, food, distance)
    }
}

class VipCustomer(name: String) : Customer(name) {

    override fun pay(amount: Double): Boolean {
        val discountedPrice = amount * 0.9
        println("$name (VIP) получает скидку 10%. Цена со скидкой: $discountedPrice")
        return super.pay(discountedPrice)
    }
}

// Заказ
enum class OrderStatus {
    CREATED,
    COOKING,
    DELIVERING,
    COMPLETED
}

class Order(
    val customer: Customer,
    val courier: Courier,
    val food: Food,
    val distance: Int
) {
    private var status = OrderStatus.CREATED

    fun process() {
        status = OrderStatus.COOKING
        food.cook()

        status = OrderStatus.DELIVERING
        courier.deliver(this)

        status = OrderStatus.COMPLETED
        println("Заказ ${food.name} для ${customer.name} завершён.")
    }
}

// Регистрация
fun registerCustomer(): Customer {
    val name = readNonEmptyString("Введите имя: ")
    val isVip = readNonEmptyString("Вы VIP? (да / нет): ").lowercase() == "да"

    val customer: Customer = if (isVip) {
        println("Создан VIP-покупатель")
        VipCustomer(name)
    } else {
        println("Создан обычный покупатель")
        Customer(name)
    }

    while (true) {
        val amount = readPositiveDouble("Пополните баланс (обязательно > 0): ")
        customer.topUp(amount)
        break
    }

    println()
    return customer
}

// Проверки
fun readNonEmptyString(text: String): String {
    while (true) {
        print(text)
        val input = readLine()?.trim()
        if (!input.isNullOrEmpty()) return input
        println("Ошибка: строка не может быть пустой")
    }
}

fun readPositiveDouble(text: String): Double {
    while (true) {
        print(text)
        val input = readLine()?.toDoubleOrNull()
        if (input != null && input > 0) return input
        println("Ошибка: введите положительное число")
    }
}

fun readPositiveInt(text: String): Int {
    while (true) {
        print(text)
        val input = readLine()?.toIntOrNull()
        if (input != null && input > 0) return input
        println("Ошибка: введите положительное целое число")
    }
}

fun formatMinutes(minutes: Int): String {
    val lastDigit = minutes % 10
    val lastTwoDigits = minutes % 100

    val word = when {
        lastTwoDigits in 11..14 -> "минут"
        lastDigit == 1 -> "минута"
        lastDigit in 2..4 -> "минуты"
        else -> "минут"
    }
    return "$minutes $word"
}