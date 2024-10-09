package main

class ItemController(private val itemService: ItemService) {

    fun quiz(numberOfQuestions: Int) {
        val selectedItems = itemService.selectRandomItems(numberOfQuestions)
        var correctAnswers = 0

        selectedItems.forEachIndexed { index, item ->
            println("${index + 1}. ${item.question}")
            item.answers.forEachIndexed { idx, answer -> println("$idx. $answer") }

            print("Your answer (index): ")
            val userAnswer = readlnOrNull()?.toIntOrNull()

            if (userAnswer == item.correct) {
                println("Correct!\n")
                correctAnswers++
            } else {
                println("Wrong! The correct answer was: ${item.answers[item.correct]}\n")
            }
        }

        println("Quiz finished! You got $correctAnswers out of $numberOfQuestions correct.")
    }
}
