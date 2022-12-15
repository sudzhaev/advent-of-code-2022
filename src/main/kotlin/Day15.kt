import kotlin.math.abs

const val MANHATTAN_PI = 4

data class Circle(val center: Coordinate, val radius: Int) {

    fun contains(coordinate: Coordinate): Boolean {
        return abs(center.x - coordinate.x) + abs(center.y - coordinate.y) <= radius
    }
}

data class Sensor(val x: Int, val y: Int) {

    fun distanceTo(beacon: Beacon): Int {
        return abs(x - beacon.x) + abs(y - beacon.y)
    }

    fun circle(radius: Int): Set<Coordinate> {
        val circle = LinkedHashSet<Coordinate>(2 * MANHATTAN_PI * radius)
        var yDeviation = 0
        for (currentX in x - radius..x + radius) {
            circle += currentX to y + yDeviation
            circle += currentX to y - yDeviation
            if (currentX >= x) {
                yDeviation--
            } else {
                yDeviation++
            }
        }
        return circle
    }
}

data class Beacon(val x: Int, val y: Int) {
    fun asCoordinate() = x to y
}

fun day15() {
    fun parse(line: String): Pair<Sensor, Beacon> {
        val split = line.split(" ")
        val sensorX = split[2].drop(2).dropLast(1).toInt()
        val sensorY = split[3].drop(2).dropLast(1).toInt()
        val beaconX = split[8].drop(2).dropLast(1).toInt()
        val beaconY = split[9].drop(2).toInt()
        return Sensor(sensorX, sensorY) to Beacon(beaconX, beaconY)
    }

    val targetY = 2_000_000

    val xValues = mutableSetOf<Int>()
    val xBeaconValues = mutableSetOf<Int>()
    val circles = mutableListOf<Circle>()

    readStdin { line ->
        val (sensor, beacon) = parse(line)
        val distance = sensor.distanceTo(beacon)
        val circle = sensor.circle(distance)
        circles += Circle(sensor.x to sensor.y, distance)
        val yCirclePoints = circle.filter { it.y == targetY }
        if (yCirclePoints.isNotEmpty()) {
            when (yCirclePoints.size) {
                1 -> xValues += yCirclePoints[0].x
                2 -> {
                    val point1 = yCirclePoints[0]
                    val point2 = yCirclePoints[1]
                    for (x in point1.x..point2.x) {
                        xValues += x
                    }
                }

                else -> error("there cannot be more than 2 points")
            }
        }
        if (beacon.y == targetY) {
            xBeaconValues += beacon.x
        }
    }

    println((xValues - xBeaconValues).size)

    var targetBeacon: Coordinate? = null
    loop@
    for (x in 0..4_000_000) {
        var y = 0
        while (y <= 4_000_000) {
            val coordinate = x to y
            val circle = circles.firstOrNull { it.contains(coordinate) }
            if (circle == null) {
                targetBeacon = coordinate
                break@loop
            }

            val xDistance = circle.center.x - x
            y = circle.center.y + circle.radius - xDistance + 1
        }
    }
    check(targetBeacon != null) { "target beacon not found" }
    println(targetBeacon)
}

fun main() {
    day15()
}
