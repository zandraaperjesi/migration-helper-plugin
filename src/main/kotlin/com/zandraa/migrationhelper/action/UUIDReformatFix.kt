package com.zandraa.migrationhelper.action

import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.xml.XmlFileImpl
import com.zandraa.migrationhelper.utils.FIND_UUID_LONG_REGEX

class UUIDReformatFix : BaseIntentionAction(), HighPriorityAction {
    override fun getFamilyName(): String = "All caps, no dash"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return (file as? XmlFileImpl)?.isValid ?: false
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        WriteCommandAction.runWriteCommandAction(project) {
            editor?.document?.replaceString(
                    0,
                    editor?.document?.getText()?.length,
                    editor?.document?.text?.replace(FIND_UUID_LONG_REGEX) {it.value.toUpperCase().replace("-", "")})
        }
    }
}