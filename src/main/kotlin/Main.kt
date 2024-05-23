import java.util.*
var scanner = Scanner(System.`in`)
val lengthUnits = listOf("m", "meter", "meters", "km", "kilometer", "kilometers", "cm", "centimeter", "centimeters", "mm", "millimeter", "millimeters", "mi", "mile", "miles", "yd", "yard", "yards", "in", "inch", "inches", "ft", "foot", "feet")
val weightUnits = listOf("g", "gram", "grams", "kg", "kilogram", "kilograms", "mg", "milligram", "milligrams", "lb", "pound", "pounds", "oz", "ounce", "ounces")

open class UnitConverter(inputValue: Double, from: String, to: String) {
    var value = inputValue
    var fromUnit = from
    var toUnit = to

    fun printResultOfConversion(convertedValue: Double) {
        println("$value ${fromUnit}${addLetterS(fromUnit, value)} is $convertedValue $toUnit${addLetterS(toUnit, convertedValue)}")
    }
}

class LengthConverter(inputValue: Double, from: String, to: String) : UnitConverter(inputValue, from, to) {
    private val unitsLength = mapOf(
        "meter" to listOf("m", "meter", "meters"),
        "kilometer" to listOf("km", "kilometer", "kilometers"),
        "centimeter" to listOf("cm", "centimeter", "centimeters"),
        "millimeter" to listOf("mm", "millimeter", "millimeters"),
        "mile" to listOf("mi", "mile", "miles"),
        "yard" to listOf("yd", "yard", "yards"),
        "feet" to listOf("ft", "foot", "feet"),
        "inch" to listOf("in", "inch", "inches"),
    )
    private val coefficientToMeter = mapOf(
        "meter" to 1.0,
        "kilometer" to 1000.0,
        "centimeter" to 0.01,
        "millimeter" to 0.001,
        "mile" to 1609.35,
        "yard" to 0.9144,
        "feet" to 0.3048,
        "inche" to 0.0254,
    )

    private fun readUnitLength(unit: String) = try {
        unitsLength.filter { unit in it.value }.keys.first()
    } catch (e: NoSuchElementException) {
        "unknown unit of length"
    }

    fun convert(): Double {
        fromUnit = readUnitLength(fromUnit)
        toUnit = readUnitLength(toUnit)
        return if (fromUnit in coefficientToMeter && toUnit in coefficientToMeter) {
            value * coefficientToMeter[fromUnit]!! / coefficientToMeter[toUnit]!!
        } else {
            -1.0
        }
    }
}

class WeightConverter(inputValue: Double, from: String, to: String) : UnitConverter(inputValue, from, to) {
    private val unitsWeigh = mapOf(
        "gram" to listOf("g", "gram", "grams"),
        "kilogram" to listOf("kg", "kilogram", "kilograms"),
        "milligram" to listOf("mg", "milligram", "milligrams"),
        "pound" to listOf("lb", "pound", "pounds"),
        "ounce" to listOf("oz", "ounce", "ounces"),
    )
    private val coefficientToGram = mapOf(
        "gram" to 1.0,
        "kilogram" to 1000.0,
        "milligram" to 0.001,
        "pound" to 453.592,
        "ounce" to 28.3495,
    )

    private fun readUnitWeight(unit: String) = try {
        unitsWeigh.filter { unit in it.value }.keys.first()
    } catch (e: NoSuchElementException) {
        "unknown unit of length"
    }

    fun convert(): Double {
        fromUnit = readUnitWeight(fromUnit)
        toUnit = readUnitWeight(toUnit)
        return if (fromUnit in coefficientToGram && toUnit in coefficientToGram) {
            value * coefficientToGram[fromUnit]!! / coefficientToGram[toUnit]!!
        } else {
            -1.0
        }
    }
}

fun addLetterS(unit: String, value: Double) =
    if (value != 1.0 && (unit !in listOf("feet", "inches"))) "s"
    else ""

fun readValueOfUnit() = try {
    scanner.nextDouble()
} catch (e: InputMismatchException) {
    try {
        scanner.next().toDouble()
    } catch (e: NumberFormatException) {
        -1.0
    }
}

fun main() {
    while (true) {
        print("Enter what you want to convert (or exit): ")
        val inputData = readln()
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

        var lengthConverter: LengthConverter
        var weightConverter: WeightConverter

        var convertedValue = -2.0


        when {
            fromUnit in lengthUnits && toUnit in lengthUnits -> {
                lengthConverter = LengthConverter(value, fromUnit, toUnit)
                convertedValue = lengthConverter.convert()
                lengthConverter.printResultOfConversion(convertedValue)
            }

            fromUnit in weightUnits && toUnit in weightUnits -> {
                weightConverter = WeightConverter(value, fromUnit, toUnit)
                convertedValue = weightConverter.convert()
                weightConverter.printResultOfConversion(convertedValue)
            }

            fromUnit !in (lengthUnits + weightUnits) && toUnit in (lengthUnits + weightUnits) ->
                println("Conversion from ??? to $toUnit${addLetterS(fromUnit, 2.0)} is impossible")

            fromUnit in (lengthUnits + weightUnits) && toUnit !in (lengthUnits + weightUnits) ->
                println("Conversion from $fromUnit${addLetterS(fromUnit, value)} to ??? is impossible")

            else -> println("Conversion from ??? to ??? is impossible")
        }

        if (convertedValue == -1.0) println("Sorry, We can't do it. Try another one unit of measures")
    }
}