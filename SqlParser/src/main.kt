import sqlparser.SqlParser

fun main() {
    //var query = "SELECT author.name, count(book.id), sum(book.cost) FROM (SELECT * FROM books, users), author RIGHT OUTER JOIN departments d ON u.d_id = d.id LEFT JOIN book ON (author.id = book.author_id)  GROUP BY author.name HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 ORDER BY column1 ASC, column2 DESC, column3 ASC LIMIT 15 OFFSET 10 ROWS;"
    val query = "SELECT d.id, d.name FROM users u RIGHT OUTER JOIN departments d ON u.d_id = d.id WHERE u.id IS null"

    val sqlParser = SqlParser()

    val selectQuery = sqlParser.parseSelectQuery(query)

    println(selectQuery)
}