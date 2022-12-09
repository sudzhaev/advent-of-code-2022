import kotlin.math.absoluteValue
import kotlin.math.sign


typealias Coordinate = Pair<Int, Int>

val Coordinate.x
    get() = first
val Coordinate.y
    get() = second

fun day9(length: Int) {
    val offsetMap = mapOf(
        'R' to (1 to 0),
        'L' to (-1 to 0),
        'U' to (0 to 1),
        'D' to (0 to -1)
    )
    val noOffset = 0 to 0

    fun move(knot: Coordinate, offset: Coordinate): Coordinate {
        if (offset == noOffset) {
            return knot
        }
        return knot.x + offset.x to knot.y + offset.y
    }

    fun getOffset(knot1: Coordinate, knot2: Coordinate): Coordinate {
        // touching is a boolean that indicates whether the two knots are touching both horizontally, vertically and diagonally
        val touching = (knot1.x - knot2.x).absoluteValue <= 1 && (knot1.y - knot2.y).absoluteValue <= 1
        if (touching) {
            return noOffset
        }
        return (knot1.x - knot2.x).sign to (knot1.y - knot2.y).sign
    }

    fun moveRope(rope: MutableList<Coordinate>, headOffset: Coordinate) {
        rope[0] = move(rope.first(), headOffset)
        for (i in rope.indices.drop(1)) {
            val offset = getOffset(rope[i - 1], rope[i])
            rope[i] = move(rope[i], offset)
        }
    }

    val rope = MutableList(length) { 0 to 0 }
    val tailPositionSet = mutableSetOf<Coordinate>()

    readStdin { line ->
        val split = line.split(' ')
        val direction = split[0].first()
        val distance = split[1].toInt()
        val offset = offsetMap[direction] ?: error("invalid direction: $direction")
        for (i in 0 until distance) {
            moveRope(rope, offset)
            tailPositionSet += rope.last()
        }
    }

    println(tailPositionSet.size)
}

// part 1 – day9(2)
// part 2 – day9(10)
