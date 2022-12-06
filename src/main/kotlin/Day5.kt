import java.io.InputStreamReader
import java.util.*

// https://adventofcode.com/2022/day/5
// that's a mess, I was doing it at 2AM
// I'll rewrite it later

fun day5part1() {
    fun parse(line: String): List<String> {
        return line.chunked(4).map { it.trim(' ', ']', '[') }
    }

    fun parseInitialStateMap(rawInitialStateMap: List<List<String>>): List<Stack<String>> {
        val initialStateMap = mutableListOf<Stack<String>>()
        for (i in rawInitialStateMap[0].indices) {
            initialStateMap.add(Stack())
        }
        for (strings in rawInitialStateMap.reversed()) {
            for ((i, string) in strings.withIndex()) {
                if (string.isNotEmpty()) {
                    initialStateMap[i].push(string)
                }
            }
        }
        return initialStateMap
    }

    val lines = InputStreamReader(System.`in`).readLines()
    val rawInitialStateMap = mutableListOf<List<String>>()
    var stopIdx = 0;
    for ((i, line) in lines.withIndex()) {
        if (line.isEmpty()) {
            stopIdx = i
            break
        }
        if (!line.contains('[')) {
            continue
        }
        rawInitialStateMap += parse(line)
    }

    val initialStateMap = parseInitialStateMap(rawInitialStateMap)
    println(initialStateMap)

    for (line in lines.drop(stopIdx + 1)) {
        val split = line.split(" ")
        val n = split[1].toInt()
        val source = split[3].toInt()
        val sourceStack = initialStateMap[source - 1]
        val target = split[5].toInt()
        val targetStack = initialStateMap[target - 1]

        val itemsToMove = mutableListOf<String>()
        for (i in 0 until n) {
            // part 1 move
            // targetStack.push(sourceStack.pop())
            itemsToMove += sourceStack.pop()
        }

        for (s in itemsToMove.reversed()) {
            targetStack.push(s)
        }
    }

    println(initialStateMap.map { it.peek() }.joinToString(""))
}

fun main() {
    day5part1()
}