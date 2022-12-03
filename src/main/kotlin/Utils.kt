import java.io.InputStreamReader

fun readStdin(block: (String) -> Unit) {
    InputStreamReader(System.`in`).forEachLine(block)
}
