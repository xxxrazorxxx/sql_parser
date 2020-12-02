package sql

class Sort(
    private val column: String = "",
    private val type: String = ""
) {
    override fun toString(): String {
        return "$column ${type.toUpperCase()}"
    }
}