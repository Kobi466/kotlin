package lab_1

class Rectangle(
    var width: Double,
    var height: Double
) : Shape {
    override fun calculateArea(): Double {
        return height * width
    }

    override fun calculatePerimeter(): Double = 2 * (height + width)

}