import java.io.InputStreamReader

interface FileSystemEntry {
    val parent: FileSystemEntry?
    val name: String
    fun size(directoryToSizeMap: MutableList<Int>): Int

    fun addChild(entry: FileSystemEntry)

    fun findChild(name: String): FileSystemEntry
}

class Directory(override val name: String, override val parent: FileSystemEntry?) : FileSystemEntry {
    override fun size(directoryToSizeMap: MutableList<Int>): Int {
        val size = children.sumOf { it.size(directoryToSizeMap) }
        directoryToSizeMap += size
        return size
    }

    private val children = mutableListOf<FileSystemEntry>()

    override fun addChild(entry: FileSystemEntry) {
        children += entry
    }

    override fun findChild(name: String): FileSystemEntry {
        return children.find { it.name == name }!!
    }
}

class File(override val name: String, val size: Int, override val parent: FileSystemEntry?) : FileSystemEntry {
    override fun size(directoryToSizeMap: MutableList<Int>): Int {
        return size
    }

    override fun addChild(entry: FileSystemEntry) {
        error("no children of file")
    }

    override fun findChild(name: String): FileSystemEntry {
        error("no children of file")
    }
}

// skipping
// $ cd /
// $ ls
fun readStdin7(block: (String) -> Unit) {
    InputStreamReader(System.`in`).useLines { lines -> lines.drop(2).forEach(block) }
}

fun day7() {
    val root = Directory("/", null)
    var currentEntry: FileSystemEntry = root
    readStdin7 { line ->
        if (line.startsWith("$")) {
            val command = line.drop(2)
            if (command.startsWith("cd")) {
                val directory = command.split(" ")[1]
                currentEntry = when (directory) {
                    ".." -> currentEntry.parent!!
                    else -> currentEntry.findChild(directory)
                }
                return@readStdin7
            }
            if (command == "ls") {
                return@readStdin7
            }
        }

        val split = line.split(" ")
        val fileSize = split[0].toIntOrNull()
        if (fileSize != null) {
            val fileName = split[1]
            val file = File(fileName, fileSize, currentEntry)
            currentEntry.addChild(file)
        }

        if (line.startsWith("dir")) {
            val directoryName = line.drop(4)
            val directory = Directory(directoryName, currentEntry)
            currentEntry.addChild(directory)
        }
    }

    val sizeList = mutableListOf<Int>()
    root.size(sizeList)

    // part 1
    println(sizeList.filter { it < 100_000 }.sum())

    // part 2
    val freeSpaceSize = 70_000_000 - sizeList.last()
    val spaceToFree = 30_000_000 - freeSpaceSize
    println(sizeList.filter { it > spaceToFree }.minBy { it - spaceToFree })
}

fun main() {
    day7()
}
