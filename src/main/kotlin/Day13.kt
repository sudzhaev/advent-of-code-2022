sealed interface PacketValue {
    data class Number(val value: Int) : PacketValue {
        override fun isBefore(other: PacketValue): Int {
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

    data class Array(val values: List<PacketValue>) : PacketValue {
        override fun isBefore(other: PacketValue): Int {
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

    infix fun isBefore(other: PacketValue): Int
}

fun parsePacket(packet: String): Pair<PacketValue, Int> {
    val list = mutableListOf<PacketValue>()
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
                    list += PacketValue.Number(number)
                }
                return Pair(PacketValue.Array(list), actualIdx + 1)
            }

            ',' -> {
                if (number != null) {
                    list += PacketValue.Number(number)
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
    fun parse(lines: List<String>): Pair<PacketValue, PacketValue> {
        val packet1 = lines[0]
        val packet2 = lines[1]
        return parsePacket(packet1).first to parsePacket(packet2).first
    }

    var totalSum = 0
    var index = 1
    chunkReadStdin(3) { lines ->
        val (value1, value2) = parse(lines)
        if (value1 isBefore value2 == -1) {
            totalSum += index
        }
        index++
    }
    println(totalSum)
}

fun main() {
    day13()
}
