package com.zandraa.migrationhelper.util

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager

fun insertTextAtCaret(caret: Caret, text: CharSequence, project: Project) {
    val textLength = text.length
    val start: Int
    val document = caret.editor.document
    if (caret.hasSelection()) {
        start = caret.selectionStart
        val end = caret.selectionEnd

        WriteCommandAction.runWriteCommandAction(
            project
        ) {
            document.replaceString(start, end, text)
            PsiDocumentManager.getInstance(project).commitDocument(document)
        }
        caret.setSelection(start, start + textLength)
    } else {
        start = caret.offset
        WriteCommandAction.runWriteCommandAction(
            project
        ) {
            document.insertString(start, text)
            PsiDocumentManager.getInstance(project).commitDocument(document)
        }
    }
    caret.moveToOffset(start + textLength)
}