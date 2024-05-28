package converter

import java.lang.StringBuilder
import java.util.*

var scanner = Scanner(System.`in`)

val unitsWeight = mapOf(
    "gram" to listOf("g", "gram", "grams"),
    "kilogram" to listOf("kg", "kilogram", "kilograms"),
    "milligram" to listOf("mg", "milligram", "milligrams"),
    "pound" to listOf("lb", "pound", "pounds"),
    "ounce" to listOf("oz", "ounce", "ounces")
)
val unitsLength = mapOf(
    "meter" to listOf("m", "meter", "meters"),
    "kilometer" to listOf("km", "kilometer", "kilometers"),
    "centimeter" to listOf("cm", "centimeter", "centimeters"),
    "millimeter" to listOf("mm", "millimeter", "millimeters"),
    "mile" to listOf("mi", "mile", "miles"),
    "yard" to listOf("yd", "yard", "yards"),
    "feet" to listOf("ft", "foot", "feet"),
    "inches" to listOf("in", "inch", "inches")
)
val unitsTemperature = mapOf(
    "Celsius" to listOf( "degree celsius", "degrees celsius", "celsius", "dc", "c", "Celsius"),
    "Fahrenheit" to listOf("degree fahrenheit", "degrees fahrenheit", "fahrenheit", "df", "f", "Fahrenheit"),
    "kelvin" to listOf("kelvin", "kelvins", "k")
)
val coefficientsWeight = mapOf(
    "gram" to 1.0,
    "kilogram" to 1000.0,
    "milligram" to 0.001,
    "pound" to 453.592,
    "ounce" to 28.3495,
)
val coefficientsLength = mapOf(
    "meter" to 1.0,
    "kilometer" to 1000.0,
    "centimeter" to 0.01,
    "millimeter" to 0.001,
    "mile" to 1609.35,
    "yard" to 0.9144,
    "feet" to 0.3048,
    "inches" to 0.0254
)


open class UnitConverter(
    protected open var units: Map<String, List<String>>,
    protected open var coefficients: Map<String, Double> = emptyMap()
) {
    private var value = 0.0
    private var fromUnit = "from"
    private var toUnit = "to"
    private var convertedValue = 0.0

    fun setFromUnit(unit: String) {
        fromUnit = readUnit(unit)
    }
    fun setToUnit(unit: String) {
        toUnit = readUnit(unit)
    }
    fun getFromUnit() = fromUnit
    fun getToUnit() = toUnit

    private fun readUnit(unit: String) = try {
        units.filter { unit in it.value }.keys.first()
    } catch (e: NoSuchElementException) {
        "???"
    }

    fun printResultOfConversion() {
        when (convertedValue) {
            -1.0 -> println("Sorry, We can't do it. Try another one unit of measures")
            else -> {
                if (fromUnit == "feet" && value == 1.0) fromUnit = "foot"
                if (toUnit == "feet" && convertedValue == 1.0) toUnit = "foot"
                println("$value ${fromUnit}${addLetterS(fromUnit, value)} is $convertedValue $toUnit${addLetterS(toUnit, convertedValue)}")
            }
        }
    }

    open fun convert(value: Double) {
        this.value = value
        convertedValue = when {
            fromUnit in coefficients && toUnit in coefficients ->
                this.value * coefficients[fromUnit]!! / coefficients[toUnit]!!
            else -> -1.0
        }
    }
}
class LengthConverter : UnitConverter(unitsLength, coefficientsLength) {
    fun availableUnits(): List<String> {
        val availableUnits = mutableListOf<String>()
        for (unit in unitsLength.values) {
            unit.forEach { availableUnits.add(it) }
        }
        return availableUnits.toList()
    }
}
class WeightConverter : UnitConverter(unitsWeight, coefficientsWeight) {
    fun availableUnits(): List<String> {
        val availableUnits = mutableListOf<String>()
        for (unit in unitsWeight.values) {
            unit.forEach { availableUnits.add(it) }
        }
        return availableUnits.toList()
    }
}
class TempConverter : UnitConverter(unitsTemperature) {
    fun availableUnits(): List<String> {
        val availableUnits = mutableListOf<String>()
        for (unit in unitsTemperature.values) {
            unit.forEach { availableUnits.add(it) }
        }
        return availableUnits.toList()
    }
    override fun convert(value: Double) {
        println("From = ${getFromUnit()}, To = ${getToUnit()}, Value = $value")
    }
}

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

fun addLetterS(unit: String, value: Double) =
    if (value != 1.0 && (unit !in listOf("feet", "inches")) && unit != "???") "s"
    else ""

fun main() {
    val lengthConverter = LengthConverter()
    val weightConverter = WeightConverter()
    val tempConverter = TempConverter()

    val availableWeightUnits = weightConverter.availableUnits()
    val availableLengthUnits = lengthConverter.availableUnits()
    val availableTempUnits = tempConverter.availableUnits()

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

        var fromUnit = buildString {
            append(scanner.next())
            val word = scanner.next()
            if (word != "to") {
                append(" ")
                append(word)
            }

        }
        var toUnit = inputData.substringAfter("to ")

        lengthConverter.setFromUnit(fromUnit)
        weightConverter.setFromUnit(fromUnit)
        tempConverter.setFromUnit(fromUnit)
        lengthConverter.setToUnit(toUnit)
        weightConverter.setToUnit(toUnit)
        tempConverter.setToUnit(toUnit)

        val lF = lengthConverter.getFromUnit()
        val wF = weightConverter.getFromUnit()
        val tF = tempConverter.getFromUnit()
        println(tF)
        val lT = lengthConverter.getToUnit()
        val wT = weightConverter.getToUnit()
        val tT = tempConverter.getToUnit()
        println(tT)

        when {
            lF in availableLengthUnits && lT in availableLengthUnits -> {
                lengthConverter.convert(value)
                lengthConverter.printResultOfConversion()
            }

            wF in availableWeightUnits && wT in availableWeightUnits -> {
                weightConverter.convert(value)
                weightConverter.printResultOfConversion()
            }
            tF in availableTempUnits && tT in availableTempUnits -> {
                tempConverter.convert(value)
                tempConverter.printResultOfConversion()
            }
            else -> {
                if (lF == "???" && wF != "???") fromUnit = wF
                if (wF == "???" && lF != "???") fromUnit = lF
                if (lT == "???" && wT != "???") toUnit = wT
                if (wT == "???" && lT != "???") toUnit = lT
                if (wF == "???" && lF == "???") fromUnit = "???"
                if (wT == "???" && lT == "???") toUnit = "???"
                println("Conversion from $fromUnit${addLetterS(fromUnit, 2.0)} to $toUnit${addLetterS(toUnit, 2.0)} is impossible")
            }
        }
    }
}