import java.util.*
import kotlin.math.max

// https://adventofcode.com/2022/day/1

fun day1part1() {
    var maxCalories = 0
    var currentCalories = 0
    readStdin { line ->
        val calories = line.toIntOrNull()
        if (calories != null) {
            currentCalories += calories
        } else {
            maxCalories = max(maxCalories, currentCalories)
            currentCalories = 0
        }
    }
    println("max calories carried by the elf: $maxCalories")
}

fun day1part2() {
    val top3Elves = PriorityQueue<Int>()

    fun appendCalories(calories: Int) {
        if (top3Elves.size < 3) {
            top3Elves.add(calories)
            return
        }
        if (top3Elves.peek() < calories) {
            top3Elves.poll()
            top3Elves.add(calories)
        }
    }

    var currentCalories = 0
    readStdin { line ->
        val calories = line.toIntOrNull()
        if (calories != null) {
            currentCalories += calories
        } else {
            appendCalories(currentCalories)
            currentCalories = 0
        }
    }

    println("sum of calories carried by top 3 elves: ${top3Elves.sum()}")
}
