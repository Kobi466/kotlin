package lab_1

open class Person(
    var name: String,
    var age: Int,
    var address: String
) {
    fun printInfo() {
        println("Name: $name, Age: $age, Address: $address")
    }
}