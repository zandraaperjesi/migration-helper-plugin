package com.zandraa.migrationhelper.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.xml.XmlFileImpl
import com.zandraa.migrationhelper.action.DateValueFix
import com.zandraa.migrationhelper.utils.SpecialValueUtil.findDateValues
import com.zandraa.migrationhelper.utils.SpecialValueUtil.valueTextRange
import java.awt.Color

class ValueDateAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        val rawText = (element as? XmlFileImpl)
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
    rawText.findDateValues().forEach { (matchingValue, range) ->
        val textRange = range.valueTextRange(startOffset)
        holder.newAnnotation(HighlightSeverity.WEAK_WARNING, "This seems to be a date value, that should be in a 'valueDate' tag.")
                .range(textRange)
                .withFix(DateValueFix(textRange))
                .enforcedTextAttributes(TextAttributes(null, null, Color.ORANGE, EffectType.WAVE_UNDERSCORE, 0))
                .create()
    }
}
