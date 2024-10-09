package main

class ItemService(private val itemRepository: ItemRepository) {
    fun selectRandomItems(number: Int): List<Item> {
        if(number > itemRepository.size()) {
            println("There are no $number questions.")
            return emptyList()
        }
        return (1..number).map { itemRepository.randomItem() }
    }
}
