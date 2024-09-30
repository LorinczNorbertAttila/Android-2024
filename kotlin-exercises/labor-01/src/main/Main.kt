import java.util.Base64
import kotlin.math.sqrt
import kotlin.random.Random

fun main() {
    //1
    println("1.")
    val sum = 2+3
    println("Sum = $sum")
    println("Sum_2 = ${2+3}")
    println("\n")

    //2
    println("2.")
    val daysOfWeek = listOf("monday","Tuesday","Wednesday","Thusday","Friday","Saturday","sunday")
    for (day: String in daysOfWeek){
        println(day)
    }
    println("Days starting with letter ‘T’:")
    daysOfWeek.filter{it.startsWith("T")}.forEach{
        println(it)
    }
    println("Days containing the letter ‘e’:")
    daysOfWeek.filter{it.contains("e")}.forEach{
        println(it)
    }
    println("Days of length 6:")
    daysOfWeek.filter{it.length == 6}.forEach{
        println(it)
    }
    println("\n")
    //3.
    println("3.")
    for (i in 1..100){
        if(isPrime(i)){
            println("$i is prime")
        }
    }
    println("\n")
    //4.
    println("4.")
    val message = "This is a text"
    val encodedMessage = messageCoding(message, ::encodeString)
    println("Encoded message: $encodedMessage")
    val decodedMessage = messageCoding(encodedMessage, ::decodeString)
    println("Decoded message: $decodedMessage")
    println("\n")

    //5.
    println("5.")
    val numbers = intArrayOf(0,1,2,3,4,5,6,7,8)
    println("Even numbers: ")
    printEvenNumbers(numbers)
    println("\n")


    //6.
    println("6.")
    val listOfNumbers = listOf(1,2,3,4,5,6,7,8)
    println("List: $listOfNumbers"  + " Double elements: " + listOfNumbers.map { it*2 })
    println("Days of week capitalized: " + daysOfWeek.map { it.uppercase() })
    println("First character of each day: " + daysOfWeek.map { it.capitalize() })
    println("Length of days: " + daysOfWeek.map { it.length })
    println("Average length of days: " + daysOfWeek.map { it.length }.average())
    println("\n")

    //7.
    println("7.")
    val mutableDays = daysOfWeek.map { it.capitalize() }.toMutableList()
    mutableDays.removeAll { it.contains('n') }
    print("Days without letter 'n': $mutableDays")
    for ((index, day) in mutableDays.withIndex()) {
        println("Item at $index is $day")
    }
    mutableDays.sort()
    println("Days sorted: $mutableDays")
    println("\n")

    //8.
    println("8.")
    val randomArray = IntArray(10) { Random.nextInt(0, 101) }
    println("10 random number: ")
    randomArray.forEach { println(it) }
    println("Sorted: ")
    randomArray.sort()
    randomArray.forEach { println(it) }
    val isEven: (Int) -> Boolean = { it % 2 == 0 }
    if(randomArray.any(isEven)){
        println("It contains even number")
    }
    else{
        println("It doesn't contain any even number")
    }
    if(randomArray.all(isEven)){
        println("All numbers are even")
    }
    else{
        println("Not all numbers are even")
    }
    val average = randomArray.average()
    arrayOf(average).forEach { println("Average: $it") }
    println("\n")

}

//3.function
fun isPrime(number: Int): Boolean {
    if (number < 2) return false
    for (i in 2..sqrt(number.toDouble()).toInt()) {
        if (number % i == 0) return false
    }
    return true
}

//5.function
fun printEvenNumbers(numbers: IntArray) = numbers.filter{ it % 2 == 0}.forEach{ println( it ) }

//4.function
fun messageCoding(msg: String, func: (String) -> String): String {
    return func(msg)
}

fun encodeString(message: String): String {
    return Base64.getEncoder().encodeToString(message.toByteArray())
}
fun decodeString(encodedMessage: String): String {
    return String(Base64.getDecoder().decode(encodedMessage))
}

//8.functions
