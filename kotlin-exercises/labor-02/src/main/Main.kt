package main

import java.time.Year
import kotlin.random.Random

fun main(){
    /*
    val dict: IDictionary = ListDictionary
    val dict: IDictionary = DictionaryProvider.createDictionary(DictionaryType.HASH_SET)
    println("Number of words: ${dict.size()}")
    var word: String?
    while(true){
        print("What to find? ")
        word = readLine()
        if( word.equals("quit")){
            break
        }
        println("Result: ${word?.let { dict.find(it) }}")
    }

    val name = "John Smith";
    println(name.nameMonogram());

    val fruits = listOf("apple", "pear", "melon")
    val result = fruits.joinElements("-")
    println(result)



    val fruits2 = listOf("apple", "pear", "strawberry", "melon")
    val longestFruit = fruits2.getLongestElement()
    println(longestFruit)

 */
    //Date Class
    //1.
    val customDate = Date()
    println(customDate)
    println()

    //2.
    val customDate1 = Date(2024, 1, 15)
    val customDate2 = Date(2023, 5, 28)
    println("${customDate1.year} is a leap year: ${customDate1.isLeapYear()}")  // true
    println("${customDate2.year} is a leap year: ${customDate2.isLeapYear()}")  // false
    println()

    //3.
    val invalidDate = Date(2023, 2, 29)
    println("Is ${invalidDate.year}-${invalidDate.month}-${invalidDate.day} valid? ${invalidDate.isValid()}")
    println()

    //4.
    val validDates = mutableListOf<Date>()

    while (validDates.size < 10) {
        val randomDate = generateRandomDate()

        if (randomDate.isValid()) {
            validDates.add(randomDate)
        }
    }

    //5.
    println("Valid dates:")
    validDates.forEach{println(it)}
    println()

    //6.
    println("Sorted valid dates:")
    val sortedDates = validDates.sorted()
    sortedDates.forEach{println(it)}
    println()

    //7.
    println("Reversed sorted valid dates:")
    val reversedDates = sortedDates.reversed()
    reversedDates.forEach{println(it)}
    println()

    //8.
    val customComparator = Comparator<Date> { date1, date2 ->
        when {
            date1.day != date2.day -> date1.day.compareTo(date2.day)
            date1.month != date2.month -> date1.month.compareTo(date2.month)
            else -> date1.year.compareTo(date2.year)
        }
    }
    val sortedByCustomOrder = validDates.sortedWith(customComparator)
    println("Sorted by day:")
    sortedByCustomOrder.forEach { println(it) }

}

fun String.nameMonogram():String {
    return this.split(" ").map {it[0]}.joinToString(" ")
}
fun List<String>.joinElements(separator:String):String = this.joinToString(separator)

fun List<String>.getLongestElement():String = this.maxByOrNull { it.length } ?: "N/A"

fun Date.isLeapYear(): Boolean {
    return Year.isLeap(this.year.toLong())
}

fun Date.isValid(): Boolean {
    if (month !in 1..12) return false
    val maxDaysInMonth = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear()) 29 else 28
        else -> return false
    }
    return day in 1..maxDaysInMonth
}

fun generateRandomDate(): Date {
    val year = Random.nextInt(1500, 2024)
    val month = Random.nextInt(1, 15)
    val day = Random.nextInt(1, 40)

    return Date(year, month, day)
}
