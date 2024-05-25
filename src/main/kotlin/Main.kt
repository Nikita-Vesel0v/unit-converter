import java.util.*
var scanner = Scanner(System.`in`)

open class UnitConverter {
    private var inputTo = "input unit to"
    private var value = 0.0
    private var fromUnit = "from"
    private var toUnit = "to"
    private var convertedValue = 0.0
    protected open var units = mapOf<String, List<String>>()
    protected open var coefficients = mapOf<String, Double>()
    private var availableUnits = listOf<String>()

    init {
        availableUnits = availableUnits()
    }

    private fun availableUnits(): List<String> {
        val availableUnits = mutableListOf<String>()
        for (unit in units.values) {
            unit.forEach { availableUnits.add(it) }
        }
        return availableUnits.toList()
    }
    private fun readUnit(unit: String) =
        try {
            units.filter { unit in it.value }.keys.first()
        } catch (e: NoSuchElementException) {
            "unknown unit"
        }
    fun printResultOfConversion() {
        when (convertedValue) {
            -1.0 -> println("Sorry, We can't do it. Try another one unit of measures")
            -2.0 -> println("Conversion from $fromUnit${addLetterS(fromUnit, 2.0)} to $inputTo is impossible")
            else -> {
                if (fromUnit == "feet" && value == 1.0) fromUnit = "foot"
                if (toUnit == "feet" && convertedValue == 1.0) toUnit = "foot"
                println("$value ${fromUnit}${addLetterS(fromUnit, value)} is $convertedValue $toUnit${addLetterS(toUnit, convertedValue)}")
            }
        }
    }
    fun convert(inputValue: Double, from: String, to: String) {
        inputTo = to
        fromUnit = readUnit(from)

        toUnit = readUnit(to)

        value = inputValue
        convertedValue = when {
            toUnit == "unknown unit" -> -2.0
            fromUnit in coefficients && toUnit in coefficients ->
                value * coefficients[fromUnit]!! / coefficients[toUnit]!!
            else -> -1.0
        }
    }
    fun getAvailableUnits() = availableUnits
}

class LengthConverter : UnitConverter() {
    override var units = mapOf(
        "meter" to listOf("m", "meter", "meters"),
        "kilometer" to listOf("km", "kilometer", "kilometers"),
        "centimeter" to listOf("cm", "centimeter", "centimeters"),
        "millimeter" to listOf("mm", "millimeter", "millimeters"),
        "mile" to listOf("mi", "mile", "miles"),
        "yard" to listOf("yd", "yard", "yards"),
        "feet" to listOf("ft", "foot", "feet"),
        "inches" to listOf("in", "inch", "inches"),
    )
    override var coefficients = mapOf(
        "meter" to 1.0,
        "kilometer" to 1000.0,
        "centimeter" to 0.01,
        "millimeter" to 0.001,
        "mile" to 1609.35,
        "yard" to 0.9144,
        "feet" to 0.3048,
        "inches" to 0.0254,
    )
}

class WeightConverter : UnitConverter() {
    override var units = mapOf(
        "gram" to listOf("g", "gram", "grams"),
        "kilogram" to listOf("kg", "kilogram", "kilograms"),
        "milligram" to listOf("mg", "milligram", "milligrams"),
        "pound" to listOf("lb", "pound", "pounds"),
        "ounce" to listOf("oz", "ounce", "ounces"),
    )
    override var coefficients = mapOf(
        "gram" to 1.0,
        "kilogram" to 1000.0,
        "milligram" to 0.001,
        "pound" to 453.592,
        "ounce" to 28.3495,
    )
}

fun addLetterS(unit: String, value: Double) =
    if (value != 1.0 && (unit !in listOf("feet", "inches"))) "s"
    else ""
fun readValueOfUnit() =
    try {
        scanner.nextDouble()
    } catch (e: InputMismatchException) {
        try {
            scanner.next().toDouble()
        } catch (e: NumberFormatException) {
            -1.0
        }
}

fun main() {
    val lengthConverter = LengthConverter()
    val weightConverter = WeightConverter()

    val lengthUnits = lengthConverter.getAvailableUnits()
    val weightUnits = weightConverter.getAvailableUnits()

    while (true) {
        print("Enter what you want to convert (or exit): ")
        val inputData = readln().lowercase()
        if (inputData == "exit") break
        scanner = Scanner(inputData)
        val value = readValueOfUnit()
        if (value == -1.0) {
            println("Invalid input value. Try Again")
            continue
        }

        val fromUnit = scanner.next()
        scanner.next()
        val toUnit = scanner.next()

        when {
            fromUnit in lengthUnits -> {
                lengthConverter.convert(value, fromUnit, toUnit)
                lengthConverter.printResultOfConversion()
            }

            fromUnit in weightUnits -> {
                weightConverter.convert(value, fromUnit, toUnit)
                weightConverter.printResultOfConversion()
            }

            fromUnit !in (lengthUnits + weightUnits) && toUnit in (lengthUnits + weightUnits) ->
                println("Conversion from ??? to $toUnit${addLetterS(toUnit, 2.0)} is impossible")

            fromUnit in (lengthUnits + weightUnits) && toUnit !in (lengthUnits + weightUnits) ->
                println("Conversion from $fromUnit${addLetterS(fromUnit, value)} to ??? is impossible")

            else -> println("Conversion from ??? to ??? is impossible")
        }
    }
}