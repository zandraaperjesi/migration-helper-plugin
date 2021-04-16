package com.zandraa.migrationhelper.util

import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiField
import com.intellij.psi.PsiType
import org.jetbrains.annotations.Nullable

class XMLUtil {
    companion object {
        fun addInsert(entityFields: MutableList<PsiField>, name: @Nullable @NlsSafe String?): String {
            var generateColumnTag = entityFields.map { generateColumnTag(it) }
                .joinToString(separator = "\n    ") { it }
            return """
<insert tableName="${name?.camelToSnake()}">
    $generateColumnTag
</insert>
"""
        }

        private fun generateColumnTag(field: PsiField): String {

            return when (field.type) {
                PsiType.BOOLEAN -> """<column name="${field.name.camelToSnake()}" valueBoolean=""/>"""
                //todo date
                else -> """<column name="${field.name.camelToSnake()}" value=""/>"""
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
