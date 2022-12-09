import java.io.InputStreamReader
import kotlin.math.sign


typealias Coordinate = Pair<Int, Int>

val Coordinate.x
    get() = first
val Coordinate.y
    get() = second

fun day9(length: Int) {
    val tailPositionSet = mutableSetOf<Coordinate>()
    val offsetMap = mapOf(
        "R" to (1 to 0),
        "L" to (-1 to 0),
        "U" to (0 to 1),
        "D" to (0 to -1)
    )

    fun move(knot: Coordinate, offset: Coordinate): Coordinate {
        return knot.x + offset.x to knot.y + offset.y
    }

    fun getOffset(knot1: Coordinate, knot2: Coordinate): Coordinate {
        val touching = knot1.toList().zip(knot2.toList())
            .all { pair -> pair.x - pair.y in setOf(0, 1, -1) }
        if (touching) {
            return 0 to 0
        }
        return (knot1.x - knot2.x).sign to (knot1.y - knot2.y).sign
    }

    fun moveRope(rope: MutableList<Pair<Int, Int>>) {
        for (i in rope.indices) {
            if (i == 0) {
                continue
            }

            val offset = getOffset(rope[i - 1], rope[i])
            rope[i] = move(rope[i], offset)

            if (i == rope.size - 1) {
                tailPositionSet += rope[i]
            }
        }
    }

    val rope = (0 until length).map { 0 to 0 }.toMutableList()

    InputStreamReader(System.`in`).forEachLine { line ->
        val split = line.split(" ")
        val direction = split[0]
        val distance = split[1].toInt()
        for (i in 0 until distance) {
            rope[0] = move(rope.first(), offsetMap[direction]!!)
            moveRope(rope)
        }
    }

    println(tailPositionSet.size)
}

// part 1 – day9(2)
// part 2 – day9(10)
