import java.io.InputStreamReader

fun readStdin(block: (String) -> Unit) {
    InputStreamReader(System.`in`).forEachLine(block)
}

fun chunkReadStdin(chunkSize: Int, block: (List<String>) -> Unit) {
    InputStreamReader(System.`in`).useLines { lines ->
        lines.windowed(size = chunkSize, step = chunkSize, partialWindows = true).forEach(block)
    }
}

typealias Coordinate = Pair<Int, Int>

val Coordinate.x
    get() = first
val Coordinate.y
    get() = second
