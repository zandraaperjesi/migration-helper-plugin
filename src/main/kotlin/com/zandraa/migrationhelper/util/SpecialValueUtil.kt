package com.zandraa.migrationhelper.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange

val VALUE_WITH_DATE_REGEX = """value="(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})"""".toRegex()
val VALUE_WITH_DATE_REGEX_WITH_T = """value="(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})"""".toRegex()
val VALUE_WITH_DATE_REGEX_WITH_T_S = """value="(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})S"""".toRegex()
val VALUE_WITH_DATE_REGEX_WITH_MILLIS = """value="(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}).(\d{3})Z"""".toRegex()
val VALUE_WITH_BOOL_REGEX = """value="(true|false)"""".toRegex()

val COLUMN_ANNOTATION = """@Column\(name=\"(.*?)\"\)""".toRegex()

val VALUE_KEY = "value="
val VALUE_DATE_KEY = "valueDate="

object SpecialValueUtil {
    fun CharSequence.findDateValues(): Sequence<Pair<String, IntRange>> {
        var matches = VALUE_WITH_DATE_REGEX.findAll(this).toMutableList();
        matches.addAll(VALUE_WITH_DATE_REGEX_WITH_T.findAll(this).toMutableList())
        matches.addAll(VALUE_WITH_DATE_REGEX_WITH_T_S.findAll(this).toMutableList())
        matches.addAll(VALUE_WITH_DATE_REGEX_WITH_MILLIS.findAll(this).toMutableList())

        return matches.asSequence().map { it.value to it.range }
    }

    fun IntRange.valueTextRange(offset: Int = 0) = this.let {
        TextRange(
                offset + it.first + VALUE_KEY.length,
                offset + it.last + 1
        )
    }

    fun CharSequence.findBoolValues() = VALUE_WITH_BOOL_REGEX.findAll(this)
            .map { it.value to it.range }

    fun valueToValueDate(editor: Editor, range: TextRange) {
        editor.document.replaceString(range.startOffset - VALUE_KEY.length, range.startOffset, VALUE_DATE_KEY)
    }
}