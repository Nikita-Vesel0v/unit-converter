import java.util.*

fun kilometerToMeter(x: Int) = x * 1000

fun main() {
    val scanner = Scanner(System.`in`)

    print("Enter a number and a measure: ")

    val distance = scanner.nextInt()
    val measure = scanner.next()

    if (measure.lowercase() == "km" || measure.lowercase() == "kilometer" || measure.lowercase() == "kilometers") {
        print(distance)
        print(if (distance == 1) {
            " kilometer "
        } else {
            " kilometers "
        })
        println("is ${kilometerToMeter(distance)} meters")
    } else {
        println("Wrong input")
    }




}