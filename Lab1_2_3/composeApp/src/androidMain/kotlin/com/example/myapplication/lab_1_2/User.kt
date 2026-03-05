package lab_1

class User : Person {

    constructor(name: String, age: Int) : super(name, age, "non") {
        this.name = name
        this.age = age
    }

    fun checkAge(): Boolean {
        if (this.age >= 18) (
                return true
                ) else {
            return false
        }
    }

    fun printBorder() {
        repeat(20) {
            print("_")
        }
        println()
    }

    fun matrix(row: Int, column: Int) {
        repeat(row) {
            repeat(column) {
                print("* ")
            }
            println()
        }
    }

    override fun toString(): String {
        return "User(age=$age, name='$name')"
    }

    fun randomId(start: Int = 1, end: Int = 10000): Int {
        val randomNumber = (start..end).random()
        return randomNumber
    }

    fun convertDay(day: Int): String {
        when (day) {
            2 -> return Day.MONDAY.toString()
            3 -> return Day.TUESDAY.toString()
            4 -> return Day.WEDNESDAY.toString()
            5 -> return Day.THURSDAY.toString()
            6 -> return Day.FRIDAY.toString()
            7 -> return Day.SATURDAY.toString()
            8 -> return Day.SUNDAY.toString()
            else -> return ("Invalid day")
        }
    }

    fun grade(score: Int): String {
        return when (score) {
            in 90..100 -> "A"
            in 80..89 -> "B"
            in 70..79 -> "C"
            in 60..69 -> "D"
            else -> "F"
        }
    }
}