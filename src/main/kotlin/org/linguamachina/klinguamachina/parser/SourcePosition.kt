package org.linguamachina.klinguamachina.parser

data class SourcePosition(
    val line: Int,
    val column: Int,
    val endLine: Int,
    val endColumn: Int,
    val sourceName: String
) {
    override fun toString() = "$sourceName:($line, $column)"
}
