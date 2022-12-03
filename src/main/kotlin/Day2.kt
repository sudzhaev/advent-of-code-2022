// https://adventofcode.com/2022/day/2

private const val LOST = 0
private const val DRAW = 3
private const val WIN = 6

enum class GameOption(val request: String, val response: String, val score: Int) {
    ROCK("A", "X", 1),
    PAPER("B", "Y", 2),
    SCISSORS("C", "Z", 3)
}

enum class GameResult(val string: String) {
    LOSE("X"),
    DRAW("Y"),
    WIN("Z")
}

fun parseRequest(string: String): GameOption {
    return GameOption.values()
        .find { it.request == string }
        ?: error("$string not found")
}

fun parseResponse(string: String): GameOption {
    return GameOption.values()
        .find { it.response == string }
        ?: error("$string not found")
}

fun parseTargetResult(string: String): GameResult {
    return GameResult.values()
        .find { it.string == string }
        ?: error("$string not found")
}

fun parsePart1(string: String): Pair<GameOption, GameOption> {
    val split = string.split(" ")
    return Pair(parseRequest(split[0]), parseResponse(split[1]))
}

fun GameOption.playAgainst(otherOption: GameOption): Int {
    return score + when (this) {
        GameOption.ROCK -> when (otherOption) {
            GameOption.ROCK -> DRAW
            GameOption.PAPER -> LOST
            GameOption.SCISSORS -> WIN
        }

        GameOption.PAPER -> when (otherOption) {
            GameOption.ROCK -> WIN
            GameOption.PAPER -> DRAW
            GameOption.SCISSORS -> LOST
        }

        GameOption.SCISSORS -> when (otherOption) {
            GameOption.ROCK -> LOST
            GameOption.PAPER -> WIN
            GameOption.SCISSORS -> DRAW
        }
    }
}

fun day2part1() {
    var totalScore = 0
    readStdin { line ->
        val (request, response) = parsePart1(line)
        totalScore += response.playAgainst(request)
    }
    println("score: $totalScore")
}

fun resolveResponse(request: GameOption, targetResult: GameResult): GameOption {
    return when (request) {
        GameOption.ROCK -> when (targetResult) {
            GameResult.LOSE -> GameOption.SCISSORS
            GameResult.DRAW -> GameOption.ROCK
            GameResult.WIN -> GameOption.PAPER
        }

        GameOption.PAPER -> when (targetResult) {
            GameResult.LOSE -> GameOption.ROCK
            GameResult.DRAW -> GameOption.PAPER
            GameResult.WIN -> GameOption.SCISSORS
        }

        GameOption.SCISSORS -> when (targetResult) {
            GameResult.LOSE -> GameOption.PAPER
            GameResult.DRAW -> GameOption.SCISSORS
            GameResult.WIN -> GameOption.ROCK
        }
    }
}

fun day2part2() {
    var totalScore = 0
    readStdin { line ->
        val split = line.split(" ")
        val request = parseRequest(split[0])
        val targetResult = parseTargetResult(split[1])
        val response = resolveResponse(request, targetResult)
        totalScore += response.playAgainst(request)
    }
    println("score: $totalScore")
}
