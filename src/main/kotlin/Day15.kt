import kotlin.math.abs

fun distance(coordinate1: Coordinate, coordinate2: Coordinate): Int {
    return abs(coordinate1.x - coordinate2.x) + abs(coordinate1.y - coordinate2.y)
}

data class Circle(val center: Coordinate, val radius: Int) {

    operator fun contains(coordinate: Coordinate): Boolean {
        return distance(center, coordinate) <= radius
    }

    fun points(y: Int): List<Int> {
        val yDiff = abs(center.y - y)
        if (yDiff > radius) {
            return listOf()
        }
        val xDeviance = if (yDiff == radius) 0 else radius - yDiff
        return (center.x - xDeviance..center.x + xDeviance).toList()
    }
}

fun day15(yTarget: Int, fieldSize: Int) {
    fun parse(line: String): Pair<Coordinate, Coordinate> {
        val split = line.split(" ")
        val sensorX = split[2].drop(2).dropLast(1).toInt()
        val sensorY = split[3].drop(2).dropLast(1).toInt()
        val beaconX = split[8].drop(2).dropLast(1).toInt()
        val beaconY = split[9].drop(2).toInt()
        return (sensorX to sensorY) to (beaconX to beaconY)
    }

    fun findDistressBeacon(circles: MutableList<Circle>, fieldSize: Int): Coordinate {
        for (x in 0..fieldSize) {
            var y = 0
            while (y <= fieldSize) {
                val coordinate = x to y
                val circle = circles.firstOrNull { coordinate in it }
                    ?: return coordinate
                val xDistance = circle.center.x - x
                y = circle.center.y + circle.radius - xDistance + 1
            }
        }
        error("target beacon not found")
    }

    fun tuningFrequency(coordinate: Coordinate): Long {
        return coordinate.x.toLong() * 4_000_000L + coordinate.y.toLong()
    }

    val xValues = mutableSetOf<Int>()
    val xBeaconValues = mutableSetOf<Int>()
    val circles = mutableListOf<Circle>()

    readStdin { line ->
        val (sensor, beacon) = parse(line)
        val distance = distance(sensor, beacon)
        val circle = Circle(sensor, distance)
        circles += circle
        xValues += circle.points(yTarget)
        if (beacon.y == yTarget) {
            xBeaconValues += beacon.x
        }
    }

    println("${(xValues - xBeaconValues).size} positions cannot contain a beacon")
    val beacon = findDistressBeacon(circles, fieldSize)
    val tuningFrequency = tuningFrequency(beacon)
    println("distress beacon: $beacon, tuning frequency: $tuningFrequency")
}

// day15(2_000_000, 4_000_000)
