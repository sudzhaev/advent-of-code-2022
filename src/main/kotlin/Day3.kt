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
    readSystemIn { line ->
        totalPriority += findPriority(line)
    }
    println("total priority is $totalPriority")
}


fun day3part2() {
    fun readSystemInByChunks(chunkSize: Int, block: (List<String>) -> Unit) {
        val list = mutableListOf<String>()
        readSystemIn { line ->
            if (list.size < chunkSize) {
                list += line
            } else {
                block(list)
                list.clear()
                list.add(line)
            }
        }
        block(list)
    }

    fun findPriority(lines: List<String>): Int {
        val frequencyMap = mutableMapOf<Char, Int>()
        for (line in lines) {
            for (char in line.toSet()) {
                val currentFrequency = frequencyMap.merge(char, 1, Int::plus)
                if (currentFrequency == lines.size) {
                    return char.toPriority()
                }
            }
        }
        return 0
    }

    var totalPriority = 0
    readSystemInByChunks(3) { lines ->
        totalPriority += findPriority(lines)
    }
    println("total priority is $totalPriority")
}
