package Sql


class SelectQuery {
    var columns: List<String> = mutableListOf()
    var fromTables: List<String> = mutableListOf()
    var joins: List<Join>? = null
    var whereConditions: List<String>? = null
    var groupByColumns: List<String>? = null
    var sortColumns: List<Sort>? = null
    var having: List<String>? = null
    var limit: Int? = null
    var offset: Int? = null

    override fun toString(): String {
        val selectString = "SELECT ${columns.joinToString(", ")}"
        val fromString = " FROM ${fromTables.joinToString(", ")}"
        val joinString = joins?.let { " ${joins!!.joinToString(" ")}" } ?: ""
        val havingString = having?.let { " HAVING ${having!!.joinToString(" AND ")}" } ?: ""
        val sortString = sortColumns?.let { " ORDER BY ${sortColumns!!.joinToString(",")}" } ?: ""
        val limitString = limit?.let { " LIMIT $limit" } ?: ""
        val offsetString = offset?.let { " OFFSET $offset ROWS" } ?: ""
        val groupByString = groupByColumns?.let { " GROUP BY ${groupByColumns!!.joinToString(", ")}" } ?: ""
        val whereString = whereConditions?.let { " WHERE ${whereConditions!!.joinToString(" AND ")}" } ?: ""

        return "$selectString$fromString$joinString$whereString$groupByString$havingString$sortString$limitString$offsetString"
    }
}