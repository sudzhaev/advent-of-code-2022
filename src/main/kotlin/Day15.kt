import kotlin.math.abs

const val MANHATTAN_PI = 4

data class Sensor(val x: Int, val y: Int) {

    fun distanceTo(beacon: Beacon): Int {
        return abs(x - beacon.x) + abs(y - beacon.y)
    }

    fun circle(radius: Int): Set<Coordinate> {
        val circle = LinkedHashSet<Coordinate>(2 * MANHATTAN_PI * radius)
        var yDeviation = 0
        for (currentX in x - radius .. x + radius) {
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

    readStdin { line ->
        val (sensor, beacon) = parse(line)
        val distance = sensor.distanceTo(beacon)
        val circle = sensor.circle(distance)
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

    println((xValues - xBeaconValues).count())
}

fun main() {
    day15()
}
