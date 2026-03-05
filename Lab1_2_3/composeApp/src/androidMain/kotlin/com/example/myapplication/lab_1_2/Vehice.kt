package lab_1

abstract class Vehicle(
    var brand: String = "",
    var model: String = "",
    var year: Int = 0,
    var color: String = "",
) {
    abstract fun start()
    abstract fun stop()
    fun printInfo() {
        println("Brand: $brand, Model: $model, Year: $year, Color: $color")
    }
}