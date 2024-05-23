import java.util.*

val scanner = Scanner(System.`in`)

val unitsMeasure = mapOf(
    "meter" to listOf("m", "meter", "meters"),
    "kilometer" to listOf("km", "kilometer", "kilometers"),
    "centimeter" to listOf("cm", "centimeter", "centimeters"),
    "millimeter" to listOf("mm", "millimeter", "millimeters"),
    "mile" to listOf("mi", "mile", "miles"),
    "yard" to listOf("yd", "yard", "yards"),
    "feet" to listOf("ft", "foot", "feet"),
    "inches" to listOf("in", "inch", "inches"),
)

val coefficientToMeter = mapOf(
    "meter" to 1.0,
    "kilometer" to 1000.0,
    "centimeter" to 0.01,
    "millimeter" to 0.001,
    "mile" to 1609.35,
    "yard" to 0.9144,
    "feet" to 0.3048,
    "inches" to 0.0254,
)

fun readUnitOfMeasure() = try {
    scanner.nextDouble()
} catch(e: InputMismatchException) {
    scanner.next().toDouble()
}

fun readUnit() = try {
        val inputUnit = scanner.next().lowercase()
        unitsMeasure.filter { inputUnit in it.value }.keys.first()
    } catch (e: NoSuchElementException) {
        "unknown unit"
    }


fun toMeter(unit: String, value: Double): Double =
    if (unit in coefficientToMeter.keys) value * (coefficientToMeter[unit]!!) //use !! because check it above
    else -1.0

fun addLetterS(unit: String, value: Double) =
    if (value != 1.0 && (unit !in listOf("feet", "inches"))) "s"
    else ""

fun main() {
    print("Enter a number and a measure of length: ")

    val distance = readUnitOfMeasure()
    val unit = readUnit()

    if (unit != "unknown unit") {
        val convertedDistance = toMeter(unit, distance)
        if (convertedDistance == -1.0) {
            println("We can't convert it to meter")
        } else {
            print("$distance $unit${addLetterS(unit, distance)} is $convertedDistance meter${addLetterS("meter", convertedDistance)}")
        }
    } else {
        println("Wrong input. Unknown unit $unit")
    }
}