import java.io.InputStreamReader
import java.util.*

interface File {
    val name: String
    fun <T> accept(visitor: FileVisitor<T>): T
}

interface FileVisitor<T> {
    fun visit(plainFile: PlainFile): T
    fun visit(directory: Directory): T
}

class PlainFile(override val name: String, val size: Int) : File {
    override fun <T> accept(visitor: FileVisitor<T>): T {
        return visitor.visit(this)
    }
}

class Directory(override val name: String) : File {
    val children = mutableMapOf<String, File>()

    override fun <T> accept(visitor: FileVisitor<T>): T {
        return visitor.visit(this)
    }

    fun addChild(entry: File) {
        children[entry.name] = entry
    }

    fun findChild(name: String): File {
        return children[name] ?: error("child $name not found in directory ${this.name}")
    }
}

class DirectorySizeCollector : FileVisitor<Int> {
    val list = mutableListOf<Int>()

    override fun visit(plainFile: PlainFile): Int {
        return plainFile.size
    }

    override fun visit(directory: Directory): Int {
        val totalDirSize = directory.children.values.sumOf { it.accept(this) }
        list += totalDirSize
        return totalDirSize
    }

    fun total(): Int {
        // '/' is visited last
        return list.last()
    }
}

// skipping
// $ cd /
// $ ls
fun day7readStdin(block: (String) -> Unit) {
    InputStreamReader(System.`in`).useLines { lines -> lines.drop(2).forEach(block) }
}

private const val COMMAND_PREFIX = "$ "

fun day7() {
    val root = Directory("/")
    val visitStack = Stack<Directory>()
    var pwd = visitStack.push(root)
    day7readStdin { line ->
        if (line.startsWith(COMMAND_PREFIX)) {
            val command = line.drop(COMMAND_PREFIX.length)
            if (command.startsWith("cd")) {
                val targetDirectoryName = command.split(" ")[1]
                pwd = when (targetDirectoryName) {
                    ".." -> {
                        visitStack.pop()
                        visitStack.peek() ?: error("parent directory not found. pwd = ${pwd.name}")
                    }
                    else -> {
                        val directory = pwd.findChild(targetDirectoryName) as? Directory
                            ?: error("cannot 'cd' into non-directory")
                        visitStack.push(directory)
                        directory
                    }
                }
            } else {
                check(command == "ls")
            }
            return@day7readStdin
        }

        // processing 'ls'
        val split = line.split(" ")
        check(split.size == 2)
        val file = if (split[0] == "dir") {
            Directory(split[1])
        } else {
            val fileSize = split[0].toIntOrNull() ?: error("expected file size but got ${split[0]}")
            PlainFile(split[1], fileSize)
        }
        pwd.addChild(file)
    }

    val collector = DirectorySizeCollector()
    root.accept(collector)

    // part 1
    println(collector.list.filter { it < 100_000 }.sum())

    // part 2
    val freeSpace = 70_000_000 - collector.total()
    val spaceToFree = 30_000_000 - freeSpace
    println(collector.list.filter { it > spaceToFree }.minBy { it - spaceToFree })
}

fun main() {
    day7()
}
