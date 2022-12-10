import java.io.InputStreamReader

// https://adventofcode.com/2022/day/6/

fun day6(size: Int) {
    val buffer = ArrayDeque<Int>()

    var counter = 0;
    InputStreamReader(System.`in`).use { reader ->
        while (true) {
            val char = reader.read()
            counter++
            buffer.addLast(char)
            if (buffer.size == size) {
                if (buffer.size == buffer.toSet().size) {
                    break
                } else {
                    buffer.removeFirst()
                }
            }
        }
    }
    println("counter is $counter")
}

fun main() {
    day6(4)
    day6(14)
}
