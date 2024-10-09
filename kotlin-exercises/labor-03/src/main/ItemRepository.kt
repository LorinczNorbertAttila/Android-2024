package main
import java.io.File

object ItemRepository {
    private val items = mutableListOf<Item>()

    fun loadItemsFromFile(filename: String) {
        try {
            val file = File(filename)
            val lines = file.readLines()
            var index = 0

            while (index < lines.size) {
                // Skip any empty lines or white spaces
                if (lines[index].isBlank()) {
                    index++
                    continue
                }

                // Read the question
                val question = lines[index].trim()
                index++

                // Read the options (answers)
                val options = mutableListOf<String>()
                while (index < lines.size && lines[index].isNotBlank() && lines[index].toIntOrNull() == null) {
                    options.add(lines[index].trim())
                    index++
                }

                // Read the correct answer index
                val correctAnswerIndex = lines[index].trim().toIntOrNull() ?: -1
                index++

                // Skip any blank lines between questions
                while (index < lines.size && lines[index].isBlank()) {
                    index++
                }
                // Add the item if data is valid
                save(Item(question, options, correctAnswerIndex))
            }
        } catch (e: Exception) {
            println("Error reading file: ${e.message}")
        }
    }


    fun randomItem(): Item {
        if (getItems().isEmpty()) {
            throw IllegalStateException("No more items available!")
        }
        // Select a random item and remove it from the list to avoid duplicates
        val randomItem = items.random()
        items.remove(randomItem)
        return randomItem
    }
    private fun save(item: Item) = items.add(item)
    fun size(): Int = items.size
    private fun getItems(): List<Item> = items
}
