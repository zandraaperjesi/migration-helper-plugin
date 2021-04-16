package com.zandraa.migrationhelper.action

import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.xml.XmlFileImpl
import com.zandraa.migrationhelper.util.SpecialValueUtil

class DateValueFix(
        private val textRange: TextRange
) : BaseIntentionAction(), HighPriorityAction {
    override fun getFamilyName(): String = "value to valueDate"

    override fun getText(): String = "Change 'value' tag to 'valueBoolean'"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return (file as? XmlFileImpl)?.isValid ?: false
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile?) {
        WriteCommandAction.runWriteCommandAction(project) {
            SpecialValueUtil.valueToValueDate(editor, textRange)
        }
    }
}