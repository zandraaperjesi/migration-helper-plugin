package com.zandraa.migrationhelper.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.zandraa.migrationhelper.utils.findUUIDs
import com.zandraa.migrationhelper.utils.textRange

class UUIDAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        val rawText = (element as? LeafPsiElement)
                ?.takeIf { it.textLength >= 36 }
                ?.text
                ?.takeIf { it.isNotBlank() }
                ?: return

        highlightInText(element, rawText, holder)
    }
}

private fun highlightInText(
        element: PsiElement,
        rawText: String,
        holder: AnnotationHolder
) {
    val startOffset = element.textRange.startOffset
    rawText.findUUIDs().forEach { (matchingValue, range) ->
        val textRange = range.textRange(startOffset)
        holder.newAnnotation(HighlightSeverity.INFORMATION, "UUID")
                .range(textRange)
                .enforcedTextAttributes(DefaultLanguageHighlighterColors.CONSTANT.defaultAttributes)
                .create()
    }
}