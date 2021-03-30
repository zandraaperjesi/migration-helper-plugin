package com.zandraa.migrationhelper.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.xml.XmlTokenImpl
import com.zandraa.migrationhelper.action.UUIDReformatFix
import com.zandraa.migrationhelper.utils.findUUIDs
import com.zandraa.migrationhelper.utils.textRange
import java.awt.Color

class UUIDAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        val rawText = (element as? XmlTokenImpl)
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
                .withFix(UUIDReformatFix())
                .enforcedTextAttributes(TextAttributes(Color.CYAN, null, Color.MAGENTA, EffectType.BOLD_DOTTED_LINE, 0))
                .create()
    }
}