package lab_1

import kotlinx.coroutines.*

suspend fun main() {
    AppConfig.printConfig()
    delay(1000)
    var user = User("non", 18);
    try {
        println("Enter your name: ")
        val name = readln()
        println("Enter your age: ")
        var age = readln()
        user = User(name, age.hexToInt());
    } catch(exception: Exception) {
        println(exception.toString())
    } finally {
        println(user)
    }
    user.printInfo()

    user.printBorder()

    user.matrix(4, 5)



    if (user.checkAge()) println("${user.name} tren 18 tuoi: ${user.age}")
    else println("${user.age} chua duoi tuoi: ${user.age}")

    user.printBorder()

    println("Id cua ban: ${user.randomId()}")

    println("Nhap ngay: ")
    println(user.convertDay(readln().toInt()))

    user.printBorder()

    println("Nhap diem: ")
    val score = readln().toInt()
    println(user.grade(score))
    user.printBorder()


    println("---------List--------")
//    val user = User()
    //immutable
    val names = listOf("Kobi", "An", "Binh")

    for (name in names) {
        println(name)
    }
    names.forEach { println(it) }
    user.printBorder()

    //mutable
    val numbers = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val numbers2 = numbers.toMutableSet()

    numbers.add(6)
    numbers.remove(1)
    println(numbers)

    val peoples = mutableMapOf("Kobi" to 22, "An" to 21, "Binh" to 20)
    peoples.put("Kobi", 22)
    peoples.remove("An")

    val ids = mutableSetOf(1, 2, 3, 4, 5)
    ids.add(6)
    ids.remove(1)
    user.printBorder()

    //filter
    val evenNumbers = numbers.filter {
        it % 2 == 0
    }
    println(evenNumbers)
    user.printBorder()

    //map
    val squares = numbers.map {
        it * it
    }
    println(squares)
    user.printBorder()

    val result = numbers.filter {
        it > 5
    }.map {
        it * it + 1
    }
    println(result)
    user.printBorder()

    println("---------Set--------")
    val set = hashSetOf(1, 2, 3, 4, 5)
    println(set)
    user.printBorder()

    println("---------Map--------")
    val map = mapOf("Kobi" to 22, "An" to 21, "Binh" to 20)
    println(map)
    println(map.get("Kobi"))
    println(map["An"])
    for ((key, value) in map) {
        println("$key: $value")
    }
    user.printBorder()

    //Data class
    val store = Store("Apple", 1.99, 10)
    val store2 = store.copy(name = "Orange")
    println(store)
    println(store2)
    user.printBorder()

    //abstract class
    val car = Car("Toyota")
    car.start()
    car.stop()
    car.printInfo()
    user.printBorder()

    //interface
    val rectangle = Rectangle(5.0, 10.0)
    println("Area: ${rectangle.calculateArea()}")
    println("Perimeter: ${rectangle.calculatePerimeter()}")
    user.printBorder()

    //let
    var name2: String? = null
    name2?.let {
        println("Name: $name2")
    } ?: run {
        println("Name is null")
    }.also {
        println("continue")
    }
    user.printBorder()

    //apply
    val rectangle2 = Rectangle(123.4, 10.0).apply {
        width = 5.0
        height = 10.0
    }

    with(rectangle2) {
        println("Area: ${calculateArea()}")
        println("Perimeter: ${calculatePerimeter()}")
    }
    var area = { a: Double, b: Double -> a * b }
    println(area(5.0, 10.0))
    user.printBorder()

    //Coroutine

    //Launch parell
    delay(1000)
    runBlocking {
        launch {
            delay(1000)
            println("Task 1")
        }

        println("Task 2")
    }

    //async /await
    runBlocking {
        var result = async {
            delay(1000)
            10 + 30
        }
        println(result)
    }

    //suspend
    suspend fun fetchData(): String {
        delay(1000)
        return "Data loaded"
    }

    runBlocking {
        println(fetchData())
    }

    //Coroutine Scope
    var scope = CoroutineScope(Dispatchers.IO)

    scope.launch {
        fetchData()
    }

    val job: Job = CoroutineScope(Dispatchers.Default).launch {
        repeat(5) {
            delay(500)
            println("Running $it")
        }
    }


    runBlocking {
        delay(1500)
        job.cancel()
        println("Job cancelled")
    }


}