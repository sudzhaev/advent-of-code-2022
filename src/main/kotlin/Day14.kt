fun readObstacles(): Set<Coordinate> {
    fun parse(line: String): List<Coordinate> {
        return line.split(" -> ")
            .map { it.split(",") }
            .map { arr -> arr[0].toInt() to arr[1].toInt() }
    }

    fun numbersBetween(int1: Int, int2: Int): List<Int> {
        return if (int1 < int2) {
            (int1..int2)
        } else {
            (int1 downTo int2)
        }.toList()
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
    return rocks
}

fun day14part1(obstacles: MutableSet<Coordinate>): Int {
    fun fall(grain: Coordinate, obstacles: Set<Coordinate>): Coordinate {
        val yDown = grain.y + 1
        val down = grain.x to yDown
        if (down !in obstacles) {
            return down
        }
        val left = grain.x - 1 to yDown
        if (left !in obstacles) {
            return left
        }
        val right = grain.x + 1 to yDown
        if (right !in obstacles) {
            return right
        }
        return grain
    }

    val yMax = obstacles.maxOf { it.y }

    var sandCounter = 0
    while (true) {
        var position = 500 to 0
        var nextPosition = fall(position, obstacles)
        while (position != nextPosition && nextPosition.y < yMax) {
            position = nextPosition
            nextPosition = fall(nextPosition, obstacles)
        }
        if (nextPosition.y >= yMax) {
            break
        }
        sandCounter++
        obstacles += nextPosition
    }

    return sandCounter
}

fun day14part2(rocks: MutableSet<Coordinate>): Int {
    fun fall(grain: Coordinate, obstacles: Set<Coordinate>, yBottom: Int): Coordinate {
        val yDown = grain.y + 1
        if (yDown == yBottom) {
            return grain
        }
        val down = grain.x to yDown
        if (down !in obstacles) {
            return down
        }
        val left = grain.x - 1 to yDown
        if (left !in obstacles) {
            return left
        }
        val right = grain.x + 1 to yDown
        if (right !in obstacles) {
            return right
        }
        return grain
    }

    val yBottom = rocks.maxOf { it.y } + 2

    var sandCounter = 0
    while (true) {
        var position = 500 to 0
        var nextPosition = fall(position, rocks, yBottom)
        while (position != nextPosition) {
            position = nextPosition
            nextPosition = fall(nextPosition, rocks, yBottom)
        }
        sandCounter++
        if (position == 500 to 0) {
            break
        }
        rocks += nextPosition
    }

    return sandCounter
}

fun main() {
    val obstacles = readObstacles()
    println("part1: ${day14part1(obstacles.toMutableSet())}")
    println("part2: ${day14part2(obstacles.toMutableSet())}")
}
