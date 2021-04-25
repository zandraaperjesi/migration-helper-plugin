package com.zandraa.migrationhelper.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiType
import com.intellij.psi.impl.source.PsiJavaFileImpl
import com.zandraa.migrationhelper.dialog.InsertDialog
import com.zandraa.migrationhelper.util.COLUMN_ANNOTATION
import com.zandraa.migrationhelper.util.XMLUtil
import com.zandraa.migrationhelper.util.camelToSnake
import com.zandraa.migrationhelper.util.insertTextAtCaret
import java.lang.Exception

class GenerateInsertTagAction() : AnAction() {

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

            val times: Int = try {
                dialog.getTimes().toInt()
            } catch (e: Exception) {
                1
            }

            val chosenFilePsi = PsiManager.getInstance(project).findFile(chosenFile)
            val classes = (chosenFilePsi as PsiJavaFileImpl).classes

            val entityFields = mutableListOf<Pair<String, PsiType>>() //name,type

            for (clazz in classes) {
                val fieldList = clazz.allFields.map {
                    var fieldName = it.name.camelToSnake()
                    for (annotation in it.annotations) {
                        if (COLUMN_ANNOTATION.matches((annotation as PsiElement).text)) {
                            val (columnName) = COLUMN_ANNOTATION.find((annotation as PsiElement).text)!!.destructured
                            fieldName = columnName
                        }
                    }
                    Pair(fieldName, it.type)
                }
                entityFields.addAll(fieldList)
            }
            val addInsert = XMLUtil.addInserts(entityFields, classes[0].name, times)
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