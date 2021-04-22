package com.zandraa.migrationhelper.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.PsiJavaFileImpl
import com.zandraa.migrationhelper.dialog.InsertDialog
import com.zandraa.migrationhelper.dialog.TestDialog
import com.zandraa.migrationhelper.util.COLUMN_ANNOTATION
import com.zandraa.migrationhelper.util.XMLUtil
import com.zandraa.migrationhelper.util.insertTextAtCaret

class GenerateInsertTagAction(): AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)

        val fileChooserDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        fileChooserDescriptor.title = "Choose a file to generate from"
        fileChooserDescriptor.description = "Choose a file"

        val chosenFile = FileChooser.chooseFile(fileChooserDescriptor, project, null)

        if (project != null && chosenFile != null) {

            val dialog = InsertDialog(project)
            dialog.showAndGet()

            val times: String = dialog.getTimes()

            val chosenFilePsi = PsiManager.getInstance(project).findFile(chosenFile)
            val classes = (chosenFilePsi as PsiJavaFileImpl).classes

            val entityFields = mutableListOf<PsiField>()

            for (clazz in classes) {
                entityFields.addAll(clazz.allFields)
                for (field in clazz.allFields) {
                    var fieldName = field.name
                    for (annotation in field.annotations) {
                        if (COLUMN_ANNOTATION.matches((annotation as PsiElement).text)) {
                            println("MATCH " + (annotation as PsiElement).text)
                        }
                    }
                }
                //(clazz.allFields.get(0).annotations[1] as PsiElement).text
            }
            val addInsert = XMLUtil.addInsert(entityFields, classes[0].name)
            if (editor != null) {
                insertTextAtCaret(
                    editor.caretModel.primaryCaret,
                    addInsert,
                    project
                )
            }
        }
    }
}