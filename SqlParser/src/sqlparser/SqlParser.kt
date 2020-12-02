package sqlparser

import sql.Join
import sql.SelectQuery
import sql.Sort

class SqlParser {
    private val joinList = listOf("right", "left", "cross", "inner", "outer", "left outer", "right outer", "full outer")
    private val joinPatter = """right join|left join|cross join|outer join|left outer join|right outer join|full outer join""".toRegex()

    /**
     * Parses SelectQuery query from string
     */
    fun parseSelectQuery(Query: String): SelectQuery
    {
        val selectQuery = SelectQuery()
        var query = Query.toLowerCase().trim()

        selectQuery.columns = parseSelectColumns(query)

        if(query.indexOf("offset") >= 0){
            selectQuery.offset = parseOffset(query)
            query = query.substring(0, query.indexOf("offset")).trim()
        }

        if(query.indexOf("limit") >= 0){
            selectQuery.limit = parseLimit(query)
            query = query.substring(0, query.indexOf("limit")).trim()
        }

        if(query.indexOf("order by") >= 0){
            selectQuery.sortColumns = parseSort(query)
            query = query.substring(0, query.indexOf("order by")).trim()
        }

        if(query.indexOf("having") >= 0){
            selectQuery.having = parseHaving(query)
            query = query.substring(0, query.indexOf("having")).trim()
        }

        if(query.indexOf("group by") >= 0){
            selectQuery.groupByColumns = parseGroupBy(query)
            query = query.substring(0, query.indexOf("group by")).trim()
        }

        if(query.indexOf("where") >= 0){
            selectQuery.whereConditions = parseWhereConditions(query)
            query = query.substring(0, query.indexOf("where")).trim()
        }

        selectQuery.fromTables = parseFrom(query)

        query = query.substringAfter("from").substringAfter(selectQuery.fromTables[selectQuery.fromTables.size-1]).trim()

        if(query.indexOf("join") >= 0){
            selectQuery.joins = parseJoins(query)
        }

        return selectQuery
    }

    /**
     * Parses Select columns
     */
    private fun parseSelectColumns(query: String): List<String>{
        return query.substringAfter("select").substringBefore("from").trim().split(",").map { it.trim() }
    }

    /**
     * Parses Select offset
     */
    private fun parseOffset(query: String): Int{
        return query.substringAfter("offset").substringBefore("rows").trim().toInt()
    }

    /**
     * Parses Select limit
     */
    private fun parseLimit(query: String): Int{
        return query.substringAfter("limit").trim().toInt()
    }

    /**
     * Parses Select sort
     */
    private fun parseSort(query: String): List<Sort>{
        val result = mutableListOf<Sort>()
        val sortList = query.trim().substringAfter("order by").trim().split(",")
        sortList.forEach{
            val sortString = it.trim().split(' ')
            result.add(Sort(sortString[0], sortString[1]))
        }
        return result
    }

    /**
     * Parses Select having condition
     */
    private fun parseHaving(query: String): List<String>{
        return query.substringAfter("having").trim().split("and").map { it.trim() }
    }

    /**
     * Parses Select group by condition
     */
    private fun parseGroupBy(query: String): List<String>{
        return query.substringAfter("group by").trim().split(",").map { it.trim() }
    }

    /**
     * Parses Select where condition
     */
    private fun parseWhereConditions(query: String): List<String>{
        return query.substringAfter("where").trim().split("and").map { it.trim() }
    }

    /**
     * Parses select From tables
     */
    private fun parseFrom(query: String): List<String>{

        var fromString = query

        joinList.forEach{join ->
            if(query.indexOf(join) >= 0)
                if(query.split(join)[0].length < fromString.length)
                    fromString = query.split(join)[0]
        }

        fromString = fromString.substringAfter("from")

        val pattern = """,(?=(?:[^)]|\([^(]*\)*)*$)""".toRegex()
        val fromTables = pattern.split(fromString).filter { it.isNotBlank() }

        return fromTables.map {
            it.trim()
        }
    }

    /**
     * Parses select joins condition
     */
    private fun parseJoins(Query: String): List<Join>{
        val joinNum = (joinPatter.split(Query).size - 1)
        val result = mutableListOf<Join>()
        var query = Query

        for(i in 1..joinNum){
            val conditionString = joinPatter.split(query).first { it.isNotEmpty() }
            val type = query.substringBefore(conditionString).split("join").first()
            val (table, condition) = conditionString.split("on")

            val join = Join()
            join.type = type.trim()
            join.table = table.trim()
            join.condition = condition.trim()

            result.add(join)

            query = query.substringAfter(join.condition).trim()
        }
        return result
    }
}