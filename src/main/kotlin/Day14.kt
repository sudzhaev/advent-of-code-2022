fun day14() {
    fun parse(line: String): List<Coordinate> {
        return line.split(" -> ")
            .map { it.split(",") }
            .map { arr -> arr[0].toInt() to arr[1].toInt() }
    }

    fun numbersBetween(int1: Int, int2: Int): List<Int> {
        return if (int1 < int2) {
            (int1..int2).toList()
        } else {
            (int1 downTo int2).toList()
        }
    }

    val rocks = mutableSetOf<Coordinate>()
    readStdin { line ->
        val rockVectors = parse(line)
        for (i in 1 until rockVectors.size) {
            val prev = rockVectors[i - 1]
            val curr = rockVectors[i]
            rocks += if (prev.x == curr.x) {
                numbersBetween(prev.y, curr.y).map { prev.x to it }
            } else if (prev.y == curr.y) {
                numbersBetween(prev.x, curr.x).map { it to prev.y }
            } else {
                error("both axis are different: $prev, $curr")
            }
        }
    }

    fun fall(grain: Coordinate, rocks: Set<Coordinate>): Coordinate {
        val yDown = grain.y + 1
        val down = grain.x to yDown
        if (down !in rocks) {
            return down
        }
        val left = grain.x - 1 to yDown
        if (left !in rocks) {
            return left
        }
        val right = grain.x + 1 to yDown
        if (right !in rocks) {
            return right
        }
        return grain
    }

    val yMax = rocks.maxOf { it.y }

    var sandCounter = 0
    while (true) {
        var position = 500 to 0
        var nextPosition = fall(position, rocks)
        while (position != nextPosition && nextPosition.y < yMax) {
            position = nextPosition
            nextPosition = fall(nextPosition, rocks)
        }
        if (nextPosition.y >= yMax) {
            break
        }
        sandCounter++
        rocks += nextPosition
    }

    println(sandCounter)
}

fun main() {
    day14()
}