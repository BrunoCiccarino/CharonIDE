package org.charon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.ui.Messages
import javax.swing.JLayeredPane
import javax.swing.JPanel
import java.awt.Color

class RemoveBackgroundImageAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val editors = EditorFactory.getInstance().allEditors
        if (editors.isEmpty()) {
            Messages.showMessageDialog("No open editors found.", "Error", Messages.getErrorIcon())
            return
        }

        for (editor in editors) {
            val editorComponent = editor.component

            editorComponent.components
                .filterIsInstance<BackgroundPanel>()
                .forEach { panel ->
                    editorComponent.remove(panel)
                }

            val transparentPanel = JPanel()

            transparentPanel.setBounds(0, 0, editorComponent.width, editorComponent.height)

            editorComponent.add(transparentPanel)
            editorComponent.background = Color(255, 255, 255, 128)
            editorComponent.revalidate()
            editorComponent.repaint()
        }


    }
}
