import java.io.InputStreamReader

// https://adventofcode.com/2022/day/3

fun Char.toPriority(): Int {
    return code - (if (isLowerCase()) 96 else 38)
}

fun day3part1() {
    fun findPriority(line: String): Int {
        val charArray = line.toCharArray()
        val middleIndex = charArray.size / 2
        for (i in 0 until middleIndex) {
            val firstCompartmentItem = charArray[i]
            for (j in middleIndex until charArray.size) {
                val secondCompartmentItem = charArray[j]
                if (firstCompartmentItem == secondCompartmentItem) {
                    return firstCompartmentItem.toPriority()
                }
            }
        }
        return 0
    }

    var totalPriority = 0
    readStdin { line ->
        totalPriority += findPriority(line)
    }
    println("total priority is $totalPriority")
}


fun day3part2() {
    fun chunkReadStdin(chunkSize: Int, block: (List<String>) -> Unit) {
        InputStreamReader(System.`in`).useLines { lines ->
            lines.windowed(size = chunkSize, step = chunkSize).forEach(block)
        }
    }

    fun findPriority(lines: List<String>): Int {
        val frequencyMap = IntArray(52)
        for (line in lines) {
            for (char in line.toSet()) {
                val priority = char.toPriority()
                val frequency = ++frequencyMap[priority - 1]
                if (frequency == lines.size) {
                    return priority
                }
            }
        }
        error("no common character found")
    }

    var totalPriority = 0
    chunkReadStdin(3) { lines ->
        totalPriority += findPriority(lines)
    }
    println("total priority is $totalPriority")
}
