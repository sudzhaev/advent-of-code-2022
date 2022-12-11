import java.io.InputStreamReader


fun day8part1(grid: List<List<Int>>) {
    data class Tree(val x: Int, val y: Int)

    fun findVisibleTrees(trees: List<Int>, treeFactory: (Int) -> Tree): Set<Tree> {
        val localVisibleTrees = mutableSetOf<Tree>()
        var highestTree = -1;
        trees.forEachIndexed { idx, treeHeight ->
            if (treeHeight > highestTree) {
                highestTree = treeHeight
                localVisibleTrees += treeFactory(idx)
            }
        }
        return localVisibleTrees
    }

    val visibleTrees = mutableSetOf<Tree>()
    grid.forEachIndexed { idx, row ->
        visibleTrees += findVisibleTrees(row) { Tree(it, idx) }
        visibleTrees += findVisibleTrees(row.reversed()) { Tree(row.size - it - 1, idx) }
    }
    grid.first().forEachIndexed { idx, _ ->
        val column = grid.map { it[idx] }
        visibleTrees += findVisibleTrees(column) { Tree(idx, it) }
        visibleTrees += findVisibleTrees(column.reversed()) { Tree(idx, column.size - it - 1) }
    }
    println(visibleTrees.size)
}

private fun day8part2(grid: List<List<Int>>) {
    fun List<Int>.countScenicScore(indices: IntProgression, height: Int): Int {
        var counter = 0
        for (i in indices) {
            if (this[i] < height) {
                counter++
            } else {
                return ++counter
            }
        }
        return counter;
    }

    var highestScenicScore = 0
    grid.forEachIndexed { i, row ->
        if (i == 0 || i == grid.size - 1) {
            return@forEachIndexed
        }

        row.forEachIndexed nested@{ j, height ->
            if (j == 0 || j == row.size - 1) {
                return@nested
            }

            val left = grid[i].countScenicScore(j - 1 downTo 0, height)
            val right = grid[i].countScenicScore(j + 1 until row.size, height)
            val top = grid.map { it[j] }.countScenicScore(i - 1 downTo 0, height)
            val bottom = grid.map { it[j] }.countScenicScore(i + 1 until grid.size, height)

            val scenicScore = left * right * top * bottom
            if (scenicScore > highestScenicScore) {
                highestScenicScore = scenicScore
            }
        }
    }

    println(highestScenicScore)
}

fun day8() {
    val grid = InputStreamReader(System.`in`).readText()
        .split("\n")
        .dropLast(1)
        .map { it.map(Char::digitToInt) }
        .toList()

    day8part1(grid)
    day8part2(grid)
}
