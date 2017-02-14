/*
 * Available context bindings:
 *   COLUMNS     List<DataColumn>
 *   ROWS        Iterable<DataRow>
 *   OUT         { append() }
 *   FORMATTER   { format(row, col); formatValue(Object, col) }
 *   TRANSPOSED  Boolean
 * plus ALL_COLUMNS, TABLE, DIALECT
 *
 * where:
 *   DataRow     { rowNumber(); first(); last(); data(): List<Object>; value(column): Object }
 *   DataColumn  { columnNumber(), name() }
 */

NEWLINE = System.getProperty("line.separator")

def determineMaxWidths = { columns, rows ->
    def rowValuesMaxWidth = { column ->
        rows.collect { row ->
            def value = FORMATTER.format(row, column)

            value ? value.length() : 0
        }.max()
    }

    // { columnNumber -> max size }
    columns.collectEntries { column ->
        [column.columnNumber(), Math.max(column.name().length(), rowValuesMaxWidth(column))]
    }
}

def printResult = { columnWidthMap, columns, rows ->
    def columnWidth = { column ->
        columnWidthMap[column.columnNumber()]
    }

    // prints +----------------+-----+----------+
    def printDivider = {
        columns.each { column ->
            OUT.append("+")
            OUT.append("-" * (columnWidth(column) + 2))
        }
        OUT.append("+")
        OUT.append(NEWLINE)
    }

    // prints | Header1 | Header2 | ... |
    def printHeaderRow = {
        columns.each { column ->
            OUT.append("| ")
            OUT.append(column.name())
            OUT.append(" " * (columnWidth(column) - column.name().length() + 1))
        }
        OUT.append("|")
        OUT.append(NEWLINE)
    }

    // prints | Value1 | Value 2 | ... |
    def printRow = { row ->
        columns.each { column ->
            def value = FORMATTER.format(row, column)

            OUT.append("| ")
            OUT.append(value)
            OUT.append(" " * (columnWidth(column) - value.length() + 1))
        }
        OUT.append("|")
        OUT.append(NEWLINE)
    }

    printDivider()
    printHeaderRow()
    printDivider()
    rows.each { printRow(it) }
    printDivider()
    OUT.append(rows.size().toString()).append(" rows in set.")
}

if (TRANSPOSED) {
    throw new RuntimeException("Not implemented!")
} else {
    // we need to iterate twice (which we can't do with a single iterator) so store in a temporary
    // collection. That does mean that this export is (potentially) heavy on resource, so don't
    // do this with large result sets
    def columns = COLUMNS.collect { it }
    def rows = ROWS.collect { it }

    // map from column number to max witdth of the values in that column
    def columnWidthMap = determineMaxWidths(columns, rows)

    printResult(columnWidthMap, columns, rows)
}