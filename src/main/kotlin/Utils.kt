import java.io.InputStreamReader

fun readSystemIn(block: (String) -> Unit) {
    InputStreamReader(System.`in`).forEachLine(block)
}
