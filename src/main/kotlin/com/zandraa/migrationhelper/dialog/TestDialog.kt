package com.zandraa.migrationhelper.dialog

import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class TestDialog : DialogWrapper(true) {
    init {
        init()
        title = "Sample Dialog"
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        val label = JLabel("Press OK or Cancel")
        label.preferredSize = Dimension(100, 100)
        panel.add(label, BorderLayout.CENTER)
        return panel
    }
}