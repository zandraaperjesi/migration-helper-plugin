package com.zandraa.migrationhelper.util

import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiType
import org.jetbrains.annotations.Nullable

class XMLUtil {
    companion object {
        fun addInserts(entityFields: MutableList<Pair<String, PsiType>>, name: @Nullable @NlsSafe String?, times: Int): String {
            val insertTag = generateInsert(entityFields, name)
            var insertList = MutableList(times) {insertTag}
            return insertList.joinToString(separator = "\n") { it }
                .trimStart()
        }

        private fun generateInsert(entityFields: MutableList<Pair<String, PsiType>>, name: @Nullable @NlsSafe String?): String {
            var generateColumnTag = entityFields.map { generateColumnTag(it) }
                .joinToString(separator = "\n") { it }
            return """        <insert tableName="${name?.camelToSnake()}">
$generateColumnTag
        </insert>"""
        }

        private fun generateColumnTag(field: Pair<String, PsiType>): String {

            return when (field.second) {
                PsiType.BOOLEAN -> """            <column name="${field.first}" valueBoolean=""/>"""
                //todo date
                else -> """            <column name="${field.first}" value=""/>"""
            }
        }
    }
}

fun CharSequence.camelToSnake(): String = this.map {
    if (it.isUpperCase()) {
        "_${it.toLowerCase()}"
    } else {
        "$it"
    }
}.joinToString(separator = "").removePrefix("_")
