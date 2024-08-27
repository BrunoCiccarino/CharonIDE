package org.charon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.editor.EditorFactory
import javax.swing.ImageIcon
import javax.swing.JFileChooser
import javax.swing.JLayeredPane
import org.charon.RemoveBackgroundImageAction

class SetBackgroundImageAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return

        RemoveBackgroundImageAction().actionPerformed(e)

        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Select Background Image"
        val result = fileChooser.showOpenDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            if (file.exists()) {
                val imageIcon = ImageIcon(file.absolutePath)
                val image = imageIcon.image

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

                    val backgroundPanel = BackgroundPanel(image)
                    backgroundPanel.setBounds(0, 0, editorComponent.width, editorComponent.height)

                    val layeredPane = JLayeredPane()
                    layeredPane.setBounds(0, 0, editorComponent.width, editorComponent.height)
                    layeredPane.add(backgroundPanel, JLayeredPane.PALETTE_LAYER)

                    editorComponent.add(layeredPane)
                    editorComponent.layout = null
                    editorComponent.revalidate()
                    editorComponent.repaint()
                }

                Messages.showMessageDialog("Background image updated!", "Success", Messages.getInformationIcon())
            } else {
                Messages.showMessageDialog("Image file not found: ${file.absolutePath}", "Error", Messages.getErrorIcon())
            }
        }
    }
}
