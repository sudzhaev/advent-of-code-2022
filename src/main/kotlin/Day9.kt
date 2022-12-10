import kotlin.math.absoluteValue
import kotlin.math.sign

typealias Coordinate = Pair<Int, Int>

val Coordinate.x
    get() = first
val Coordinate.y
    get() = second

private val offsetMap = mapOf(
    'R' to (1 to 0),
    'L' to (-1 to 0),
    'U' to (0 to 1),
    'D' to (0 to -1)
)

private val noOffset = 0 to 0

interface Rope {
    fun move(offset: Coordinate): Rope
    fun tail(): Coordinate
}

class MutableRope(length: Int) : Rope {
    private val knots = MutableList(length) { 0 to 0 }

    override fun tail(): Coordinate {
        return knots.last()
    }

    override fun move(offset: Coordinate): Rope {
        knots[0] = moveKnot(knots.first(), offset)
        for (i in knots.indices.drop(1)) {
            val knotOffset = getOffset(knots[i - 1], knots[i])
            knots[i] = moveKnot(knots[i], knotOffset)
        }
        return this
    }

    private fun moveKnot(knot: Coordinate, offset: Coordinate): Coordinate {
        if (offset == noOffset) {
            return knot
        }
        return knot.x + offset.x to knot.y + offset.y
    }

    private fun getOffset(knot1: Coordinate, knot2: Coordinate): Coordinate {
        return if (isTouching(knot1, knot2)) {
            noOffset
        } else {
            (knot1.x - knot2.x).sign to (knot1.y - knot2.y).sign
        }
    }

    private fun isTouching(knot1: Coordinate, knot2: Coordinate): Boolean {
        return (knot1.x - knot2.x).absoluteValue <= 1 && (knot1.y - knot2.y).absoluteValue <= 1
    }
}

class TailTracingRope(private var rope: Rope) : Rope {

    private val tailCoordinates = mutableSetOf<Coordinate>()

    override fun move(offset: Coordinate): Rope {
        rope = rope.move(offset)
        tailCoordinates += rope.tail()
        return this
    }

    override fun tail(): Coordinate {
        return rope.tail()
    }

    fun tailPositionNumber(): Int {
        return tailCoordinates.size
    }
}

fun day9() {
    fun parse(line: String): Pair<Coordinate, Int> {
        val split = line.split(' ')
        val direction = split[0].first()
        val distance = split[1].toInt()
        val offset = offsetMap[direction] ?: error("invalid direction: $direction")
        return offset to distance
    }

    val part1rope = TailTracingRope(MutableRope(2))
    val part2rope = TailTracingRope(MutableRope(10))
    readStdin { line ->
        val (offset, distance) = parse(line)
        for (i in 1..distance) {
            part1rope.move(offset)
            part2rope.move(offset)
        }
    }
    println(part1rope.tailPositionNumber())
    println(part2rope.tailPositionNumber())
}

// part 1 – day9(2)
// part 2 – day9(10)
