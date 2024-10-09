package main

fun main() {
    val itemRepository = ItemRepository
    val itemService = ItemService(itemRepository)
    val itemController = ItemController(itemService)
    itemRepository.loadItemsFromFile("src/resources/questions.txt")

    println("Welcome to the Kotlin Quiz!")
    print("How many questions would you like to answer? ")
    val numberOfQuestions = readlnOrNull()?.toIntOrNull() ?: 5

    itemController.quiz(numberOfQuestions)
}
