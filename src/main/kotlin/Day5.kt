import java.io.InputStreamReader
import java.util.*

// https://adventofcode.com/2022/day/5
// part 1 – false
// part 2 – true
fun day5(takeAllAtOnce: Boolean) {
    fun parseState(lines: List<String>): List<Stack<Char>> {
        fun split(line: String): List<Char?> {
            return line.chunked(4) // '[A] ' or '[A]'
                .map { it.trim(' ', ']', '[') }
                .map {
                    when (it.length) {
                        0 -> null
                        1 -> it[0]
                        else -> error("invalid input: $it in line $line")
                    }
                }
        }

        fun MutableList<Stack<Char>>.getOrDefault(index: Int): Stack<Char> {
            val value = getOrNull(index)
            return value ?: Stack<Char>().also { add(it) }
        }

        val initialStateMap = arrayListOf<Stack<Char>>()
        for (line in lines.take(lines.size).reversed()) {
            for ((i, char) in split(line).withIndex()) {
                if (char != null) {
                    initialStateMap.getOrDefault(i).push(char)
                }
            }
        }
        return initialStateMap
    }

    fun parseCommand(
        line: String,
        initialStateMap: List<Stack<Char>>
    ): Triple<Int, Stack<Char>, Stack<Char>> {
        val split = line.split(" ")
        val n = split[1].toInt()
        val source = split[3].toInt()
        val sourceStack = initialStateMap[source - 1]
        val target = split[5].toInt()
        val targetStack = initialStateMap[target - 1]
        return Triple(n, sourceStack, targetStack)
    }

    val lines = InputStreamReader(System.`in`).readLines()
    val splitIndex = lines.indexOf("")
    check(splitIndex != -1) { "empty line not found" }

    val state = parseState(lines.take(splitIndex - 1))

    for (line in lines.drop(splitIndex + 1)) {
        val (n, sourceStack, targetStack) = parseCommand(line, state)

        if (takeAllAtOnce) {
            // part 2
            val buffer = arrayListOf<Char>()
            for (i in 1..n) {
                buffer += sourceStack.pop()
            }
            for (char in buffer.reversed()) {
                targetStack.push(char)
            }
        } else {
            // part 1
            for (i in 1..n) {
                targetStack.push(sourceStack.pop())
            }
        }
    }

    println(state.map { it.peek() }.joinToString(""))
}
