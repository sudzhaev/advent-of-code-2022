import java.util.*

sealed interface Packet {
    data class Number(val value: Int) : Packet {
        override fun isBefore(other: Packet): Int {
            return when (other) {
                is Number -> value.compareTo(other.value)
                is Array -> asArray() isBefore other
            }
        }

        override fun toString(): String {
            return value.toString()
        }

        fun asArray(): Array {
            return Array(listOf(this))
        }
    }

    data class Array(val values: List<Packet>) : Packet {
        override fun isBefore(other: Packet): Int {
            return when (other) {
                is Number -> isBefore(other.asArray())
                is Array -> {
                    val thisSize = values.size
                    val otherSize = other.values.size
                    for (i in 0 until minOf(thisSize, otherSize)) {
                        val thisValue = values[i]
                        val otherValue = other.values[i]
                        val comparison = thisValue isBefore otherValue
                        if (comparison != 0) {
                            return comparison
                        }
                    }
                    thisSize.compareTo(otherSize)
                }
            }
        }

        override fun toString(): String {
            return "[" + values.joinToString { it.toString() } + "]"
        }
    }

    infix fun isBefore(other: Packet): Int
}

fun parsePacket(packet: String): Pair<Packet, Int> {
    val list = mutableListOf<Packet>()
    var number: Int? = null
    var offset = 0
    for (i in packet.indices) {
        val actualIdx = i + offset
        if (actualIdx == packet.length) {
            break
        }
        when (val char = packet[actualIdx]) {
            '[' -> {
                val (value, newOffset) = parsePacket(packet.substring(actualIdx + 1))
                list += value
                offset += newOffset
            }

            ']' -> {
                if (number != null) {
                    list += Packet.Number(number)
                }
                return Pair(Packet.Array(list), actualIdx + 1)
            }

            ',' -> {
                if (number != null) {
                    list += Packet.Number(number)
                    number = null
                }
            }

            else -> {
                number = (number ?: 0) * 10 + char.digitToInt()
            }
        }
    }
    return list.first() to packet.length
}

fun day13() {
    fun parse(lines: List<String>): Pair<Packet, Packet> {
        val packet1 = lines[0]
        val packet2 = lines[1]
        return parsePacket(packet1).first to parsePacket(packet2).first
    }

    var totalSum = 0
    var index = 1
    val treeSet = TreeSet<Packet> { a, b -> a isBefore b }
    val divider1 = Packet.Array(listOf(Packet.Number(2))).also { treeSet += it }
    val divider2 = Packet.Number(6).also { treeSet += it }

    chunkReadStdin(3) { lines ->
        val (value1, value2) = parse(lines)
        if (value1 isBefore value2 == -1) {
            totalSum += index
        }
        treeSet += value1
        treeSet += value2
        index++
    }
    println("sum: $totalSum")
    treeSet.forEachIndexed {idx, value -> println("${idx + 1}: $value") }
    val decoderKey = (treeSet.indexOf(divider1) + 1) * (treeSet.indexOf(divider2) + 1)
    println("key: $decoderKey")
}

fun main() {
    day13()
}
