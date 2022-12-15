import java.util.*

sealed interface Packet : Comparable<Packet> {
    data class Number(val value: Int) : Packet {
        override fun compareTo(other: Packet): Int {
            return when (other) {
                is Number -> value.compareTo(other.value)
                is Array -> asArray() compareTo other
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
        override fun compareTo(other: Packet): Int {
            return when (other) {
                is Number -> compareTo(other.asArray())
                is Array -> {
                    val thisSize = values.size
                    val otherSize = other.values.size
                    for (i in 0 until minOf(thisSize, otherSize)) {
                        val thisValue = values[i]
                        val otherValue = other.values[i]
                        val comparison = thisValue compareTo otherValue
                        if (comparison != 0) {
                            return comparison
                        }
                    }
                    thisSize.compareTo(otherSize)
                }
            }
        }

        override fun toString(): String {
            return "[${values.joinToString { it.toString() }}]"
        }
    }
}

fun parsePacket(packet: String): Packet {
    fun parseImpl(packet: String): Pair<Packet, Int> {
        val list = mutableListOf<Packet>()
        var number: Int? = null
        var offset = 0
        for (i in packet.indices) {
            val actualIdx = i + offset
            if (actualIdx == packet.length) {
                break
            }
            val char = packet[actualIdx]
            if (char.isWhitespace()) {
                continue
            }
            when (char) {
                '[' -> {
                    val (value, newOffset) = parseImpl(packet.substring(actualIdx + 1))
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
        check(list.size == 1 || number != null)
        return (list.firstOrNull() ?: Packet.Number(number!!)) to packet.length
    }

    return parseImpl(packet).first
}

fun day13() {
    fun parse(lines: List<String>): Pair<Packet, Packet> {
        val packet1 = lines[0]
        val packet2 = lines[1]
        return parsePacket(packet1) to parsePacket(packet2)
    }

    var totalSum = 0
    val orderedPackets = TreeSet<Packet>()
    val divider1 = Packet.Array(listOf(Packet.Number(2))).also { orderedPackets += it }
    val divider2 = Packet.Array(listOf(Packet.Number(6))).also { orderedPackets += it }

    var index = 1
    chunkReadStdin(3) { lines ->
        val (value1, value2) = parse(lines)
        if (value1 < value2) {
            totalSum += index
        }
        orderedPackets += value1
        orderedPackets += value2
        index++
    }
    println("sum: $totalSum")
    val decoderKey = (orderedPackets.indexOf(divider1) + 1) * (orderedPackets.indexOf(divider2) + 1)
    println("key: $decoderKey")
}
