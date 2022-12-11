typealias MonkeyId = Int

class MonkeyTest(
    val divisibleBy: Long,
    private val targetOnTrue: MonkeyId,
    private val targetOnFalse: MonkeyId
) {

    fun getTargetMonkey(item: Long): MonkeyId {
        return if (item % divisibleBy == 0L) targetOnTrue else targetOnFalse
    }
}

data class Monkey(
    val id: MonkeyId,
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val test: MonkeyTest
)

class MonkeyBusiness(
    private val monkeyMap: Map<MonkeyId, Monkey>,
    private val relax: (Long) -> Long
) {
    private val monkeyIdList = monkeyMap.keys.sorted()
    private val inspectionRate = mutableMapOf<MonkeyId, Long>()

    fun execute(rounds: Int) {
        for (i in 1..rounds) {
            for (id in monkeyIdList) {
                val monkey = get(id)
                for (item in monkey.items) {
                    val inspectedItem = monkey.operation(item)
                    val relaxedItem = relax(inspectedItem)
                    val targetMonkey = monkey.test.getTargetMonkey(relaxedItem)
                    get(targetMonkey).items += relaxedItem
                }
                inspectionRate.merge(monkey.id, monkey.items.size.toLong(), Long::plus)
                monkey.items.clear()
            }
        }
    }

    fun level(): Long {
        return inspectionRate.values.sortedDescending().take(2).reduce(Long::times)
    }

    private fun get(id: MonkeyId): Monkey {
        return monkeyMap[id] ?: error("illegal state. $id not found")
    }
}

fun readMonkeys(): Map<MonkeyId, Monkey> {
    fun parseOperation(operationString: String): (Long) -> Long {
        val operationExpression = operationString.substring("  Operation: new = ".length).split(" ")
        check(operationExpression.size == 3)

        val leftOperand = operationExpression[0]
        check(leftOperand == "old")

        val operator: (Long, Long) -> Long = when (val rawOperator = operationExpression[1]) {
            "+" -> Long::plus
            "*" -> Long::times
            else -> error("operator '$rawOperator' not expected")
        }

        val rightOperand = operationExpression[2]
        if (rightOperand == "old") {
            return { oldValue -> operator(oldValue, oldValue) }
        } else {
            val number = rightOperand.toLong()
            return { oldValue -> operator(oldValue, number) }
        }
    }

    fun parseMonkey(monkeyDescriptor: List<String>): Monkey {
        val monkeyId = monkeyDescriptor[0].substring("Monkey ".length).dropLast(1).toInt()
        val items = monkeyDescriptor[1].substring("  Starting items: ".length)
            .split(", ")
            .map { it.toLong() }
            .toMutableList()

        val operation = parseOperation(monkeyDescriptor[2])

        val divisibleBy = monkeyDescriptor[3].substring("  Test: divisible by ".length).toLong()
        val targetOnTrue = monkeyDescriptor[4].substring("    If true: throw to monkey ".length).toInt()
        val targetOnFalse = monkeyDescriptor[5].substring("    If false: throw to monkey ".length).toInt()
        val test = MonkeyTest(divisibleBy, targetOnTrue, targetOnFalse)

        return Monkey(monkeyId, items, operation, test)
    }

    val monkeyMap = mutableMapOf<MonkeyId, Monkey>()
    chunkReadStdin(7) { monkeyDescriptor ->
        val monkey = parseMonkey(monkeyDescriptor)
        monkeyMap[monkey.id] = monkey
    }
    return monkeyMap
}

fun day11part1() {
    val monkeyMap = readMonkeys()

    val monkeyBusiness = MonkeyBusiness(monkeyMap) { it / 3 }
    monkeyBusiness.execute(20)
    println(monkeyBusiness.level())
}

fun day11part2() {
    val monkeyMap = readMonkeys()

    val commonMultiple = monkeyMap.values.map { it.test.divisibleBy }.reduce(Long::times)
    val monkeyBusiness = MonkeyBusiness(monkeyMap) { it % commonMultiple }
    monkeyBusiness.execute(10_000)
    println(monkeyBusiness.level())
}
