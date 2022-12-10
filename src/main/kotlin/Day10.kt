data class Operation(
    val ticks: Int,
    val increment: Int
)

interface TickSubscriber {
    fun onTick(x: Int, tick: Int)
}

// part 1
// result – 14720
class SignalStrengthAccumulator(private val observableTicks: Set<Int>) : TickSubscriber {
    var totalStrength = 0

    override fun onTick(x: Int, tick: Int) {
        if (tick in observableTicks) {
            totalStrength += x * tick
        }
    }
}

// part 2
// result – FZBPBFZF
class CRTDisplay(private val width: Int = 40) : TickSubscriber {
    private val crtString = StringBuilder()
    private var counter = 0

    override fun onTick(x: Int, tick: Int) {
        val visiblePixels = setOf(x, x - 1, x + 1)
        if (counter in visiblePixels) {
            crtString.append('#')
        } else {
            crtString.append('.')
        }
        counter++
        if (counter == width) {
            counter = 0
        }
    }

    fun render(consumer: (String) -> Unit = ::println) {
        val message = crtString.chunked(width).joinToString("\n")
        consumer(message)
    }
}

class TickMachine(private var x: Int = 1) {
    private var tick = 0
    private val subscribers = mutableListOf<TickSubscriber>()

    fun subscribe(subscriber: TickSubscriber) {
        subscribers += subscriber
    }

    fun execute(operation: Operation) {
        for (i in 1..operation.ticks) {
            tick++
            subscribers.forEach { it.onTick(x, tick) }
        }
        x += operation.increment
    }
}

fun day10() {
    fun parse(line: String): Operation {
        if (line == "noop") {
            return Operation(1, 0)
        }
        val split = line.split(" ")
        check(split.size == 2 && split[0] == "addx")
        return Operation(2, split[1].toInt())
    }

    val tickMachine = TickMachine()
    val signalStrengthAccumulator = SignalStrengthAccumulator(setOf(20, 60, 100, 140, 180, 220))
    tickMachine.subscribe(signalStrengthAccumulator)
    val crtDisplay = CRTDisplay()
    tickMachine.subscribe(crtDisplay)
    readStdin { line ->
        val operation = parse(line)
        tickMachine.execute(operation)
    }
    println(signalStrengthAccumulator.totalStrength)
    crtDisplay.render()
}
