import java.util.*

const val START = 'S'
const val END = 'E'

data class Day12Input(
    val heightMap: List<List<Char>>,
    val start: Coordinate,
    val end: Coordinate
)

class BFS(
    private val heightMap: List<List<Char>>,
    private val start: Coordinate,
    private val end: Coordinate
) {

    private data class Node(
        val coordinate: Coordinate,
        val value: Char,
        val distance: Int
    )

    fun distance(): Int {
        val queue: Queue<Node> = LinkedList()
        queue += Node(start, 'a', 0)

        val visited = mutableSetOf<Coordinate>()
        visited += start

        while (queue.isNotEmpty()) {
            val (coordinate, value, distance) = queue.poll()
            if (coordinate == end) {
                return distance
            }
            queue += coordinate.neighbours()
                .filter { it !in visited }
                .filter { transitionExists(source = value, target = heightMap[it]) }
                .onEach { visited += it }
                .map { Node(it, heightMap[it], distance + 1) }
        }
        return -1
    }

    private fun transitionExists(source: Char, target: Char): Boolean {
        return target - source <= 1
    }

    private fun Coordinate.neighbours(): List<Coordinate> {
        return listOf(x - 1 to y, x + 1 to y, x to y + 1, x to y - 1)
            .filter { it in heightMap }
    }

    operator fun <Char> List<List<Char>>.get(coordinate: Coordinate): Char {
        return this[coordinate.x][coordinate.y]
    }

    operator fun List<List<Char>>.contains(coordinate: Coordinate): Boolean {
        return coordinate.x >= 0 && coordinate.y >= 0
                && coordinate.x < size && coordinate.y < this[coordinate.x].size
    }

}

fun parseDay12Input(): Day12Input {
    val heightMap = mutableListOf<List<Char>>()
    var start: Coordinate? = null
    var end: Coordinate? = null

    fun initStart(rowNum: Int, row: MutableList<Char>) {
        if (start != null) return
        val startIdx = row.indexOf(START)
        if (startIdx != -1) {
            start = rowNum to startIdx
            row[startIdx] = 'a'
        }
    }

    fun initEnd(rowNum: Int, row: MutableList<Char>) {
        if (end != null) return
        val endIdx = row.indexOf(END)
        if (endIdx != -1) {
            end = rowNum to endIdx
            row[endIdx] = 'z'
        }
    }

    var rowNum = 0
    readStdin { line ->
        val row = line.toMutableList()
        heightMap += row
        initStart(rowNum, row)
        initEnd(rowNum, row)
        rowNum++
    }

    return Day12Input(heightMap, start!!, end!!)
}

fun day12() {
    val (heightMap, start, end) = parseDay12Input()
    val bfs = BFS(heightMap, start, end)
    println(bfs.distance())
}

fun main() {
    day12()
}
