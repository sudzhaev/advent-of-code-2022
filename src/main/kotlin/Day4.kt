// accepts predicate where first set always has bigger size than second set
fun day4(predicate: (Set<Int>, Set<Int>) -> Boolean) {
    fun parseRange(string: String): Set<Int> {
        val split = string.split("-")
        return (split[0].toInt()..split[1].toInt()).toSet()
    }

    // returns a pair: left set has smaller size than right
    fun <T> orderRangesBySize(set1: Set<T>, set2: Set<T>): Pair<Set<T>, Set<T>> {
        return if (set1.size < set2.size) {
            set1 to set2
        } else {
            set2 to set1
        }
    }

    fun parse(line: String): Pair<Set<Int>, Set<Int>> {
        val split = line.split(",")
        val range1 = parseRange(split[0])
        val range2 = parseRange(split[1])
        return orderRangesBySize(range1, range2)
    }

    var counter = 0;
    readStdin { line ->
        val (smallerRange, biggerRange) = parse(line)
        if (predicate(biggerRange, smallerRange)) {
            counter++
        }
    }
    println("number of predicate matches: $counter")
}

val part1predicate: (Set<Int>, Set<Int>) -> Boolean = { biggerRange, smallerRange ->
    biggerRange.containsAll(smallerRange)
}
val part2predicate: (Set<Int>, Set<Int>) -> Boolean = { biggerRange, smallerRange ->
    (biggerRange intersect smallerRange).isNotEmpty()
}
