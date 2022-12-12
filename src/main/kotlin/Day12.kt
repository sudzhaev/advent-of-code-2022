import java.util.*

class Traverser(private val heightMap: List<List<Char>>) {
    data class Node(val coordinate: Coordinate, val value: Char, val distance: Int)

    interface Strategy {
        fun stop(node: Node): Boolean
        fun canTraverse(source: Char, target: Char): Boolean
    }

    fun distance(start: Coordinate, strategy: Strategy): Int {
        val queue: Queue<Node> = LinkedList()
        queue += Node(start, heightMap[start], 0)

        val visited = mutableSetOf<Coordinate>()
        visited += start

        while (queue.isNotEmpty()) {
            val node = queue.poll()
            if (strategy.stop(node)) {
                return node.distance
            }
            queue += node.coordinate.neighbours()
                .filter { it !in visited }
                .filter { strategy.canTraverse(source = node.value, target = heightMap[it]) }
                .onEach { visited += it }
                .map { Node(it, heightMap[it], node.distance + 1) }
        }
        return -1
    }

    private fun Coordinate.neighbours(): List<Coordinate> =
        listOf(x - 1 to y, x + 1 to y, x to y + 1, x to y - 1)
            .filter { it in heightMap }

    operator fun <Char> List<List<Char>>.get(coordinate: Coordinate): Char =
        this[coordinate.x][coordinate.y]

    operator fun List<List<Char>>.contains(coordinate: Coordinate): Boolean =
        coordinate.x >= 0 && coordinate.y >= 0
                && coordinate.x < size && coordinate.y < this[coordinate.x].size
}


fun day12() {
    fun parseInput(): Triple<List<List<Char>>, Coordinate, Coordinate> {
        val heightMap = mutableListOf<List<Char>>()
        var start: Coordinate? = null
        var end: Coordinate? = null

        fun initStart(rowNum: Int, row: MutableList<Char>) {
            if (start != null) return
            val startIdx = row.indexOf('S')
            if (startIdx != -1) {
                start = rowNum to startIdx
                row[startIdx] = 'a'
            }
        }

        fun initEnd(rowNum: Int, row: MutableList<Char>) {
            if (end != null) return
            val endIdx = row.indexOf('E')
            if (endIdx != -1) {
                end = rowNum to endIdx
                row[endIdx] = 'z'
            }
        }

        var rowNum = 0
        readStdin { line ->
            val row = line.toMutableList()
            initStart(rowNum, row)
            initEnd(rowNum, row)
            heightMap += row
            rowNum++
        }

        return Triple(
            heightMap,
            start ?: error("start not initialized"),
            end ?: error("end not initialized")
        )
    }

    val (heightMap, start, end) = parseInput()
    val traverser = Traverser(heightMap)

    val part1strategy = object : Traverser.Strategy {
        override fun stop(node: Traverser.Node): Boolean {
            return node.coordinate == end
        }

        override fun canTraverse(source: Char, target: Char): Boolean {
            return target - source <= 1
        }
    }
    println("part1: ${traverser.distance(start, part1strategy)}")

    val part2strategy = object : Traverser.Strategy {
        override fun stop(node: Traverser.Node): Boolean {
            return node.value == 'a'
        }

        override fun canTraverse(source: Char, target: Char): Boolean {
            return target - source >= -1
        }
    }
    println("part2: ${traverser.distance(end, part2strategy)}")
}

fun main() {
    day12()
}
