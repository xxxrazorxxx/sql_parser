package sql

class Join {
    var type = ""
    var table = ""
    var condition = ""

    override fun toString() = "${type.toUpperCase()} JOIN $table ON $condition"
}