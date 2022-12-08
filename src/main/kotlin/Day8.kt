import java.io.InputStreamReader

fun day8() {
    // part 1
    val grid = InputStreamReader(System.`in`).readText()
        .split("\n")
        .dropLast(1)
        .map { it.toCharArray().map(Char::digitToInt) }
        .toTypedArray()

    var visibleTrees = 0;

    grid.forEachIndexed { i, row ->
        row.forEachIndexed { j, treeHeight ->
            if (i == 0 || i == grid.size - 1 || j == 0 || j == row.size - 1) {
                visibleTrees += 1
                return@forEachIndexed
            }

            val left = grid[i].subList(0, j).max()
            val right = grid[i].subList(j + 1, row.size).max()
            val top = grid.map { it[j] }.subList(0, i).max()
            val bottom = grid.map { it[j] }.subList(i + 1, grid.size).max()

            if (treeHeight > left || treeHeight > right || treeHeight > top || treeHeight > bottom) {
                visibleTrees += 1
            }

        }
    }

    // part 2
    var highestScenicScore = 0
    grid.forEachIndexed { i, row ->
        row.forEachIndexed { j, treeHeight ->
            if (i == 0 || i == grid.size - 1 || j == 0 || j == row.size - 1) {
                return@forEachIndexed
            }

            val left = grid[i].subList(0, j).reversed().countStopping { it < treeHeight }
            val right = grid[i].subList(j + 1, row.size).countStopping { it < treeHeight }
            val top = grid.map { it[j] }.subList(0, i).reversed().countStopping { it < treeHeight }
            val bottom = grid.map { it[j] }.subList(i + 1, grid.size).countStopping { it < treeHeight }

            val scenicScore = left * right * top * bottom
            if (scenicScore > highestScenicScore) {
                highestScenicScore = scenicScore
            }
        }
    }

    println(highestScenicScore)
}

fun <T> Iterable<T>.countStopping(predicate: (T) -> Boolean): Int {
    var counter = 0
    for (element in this) {
        if (predicate(element)) {
            counter++
        } else {
            counter++
            return counter
        }
    }
    return counter
}

fun main() {
    day8()
}
