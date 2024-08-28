package org.charon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.editor.EditorFactory
import javax.swing.ImageIcon
import javax.swing.JFileChooser
import javax.swing.JLayeredPane
import java.awt.Color

class SetBackgroundImageAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return

        SetTransparentBackgroundAction().actionPerformed(e)

        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Select Background Image"
        val result = fileChooser.showOpenDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            if (file.exists()) {
                val imagePath = file.absolutePath
                val imageIcon = ImageIcon(imagePath)
                val image = imageIcon.image

                com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater {
                    val editors = EditorFactory.getInstance().allEditors
                    if (editors.isEmpty()) {
                        Messages.showMessageDialog("No open editors found.", "Error", Messages.getErrorIcon())
                        return@invokeLater
                    }

                    for (editor in editors) {
                        val editorComponent = editor.component


                        editorComponent.components
                            .filterIsInstance<BackgroundPanel>()
                            .forEach { panel ->
                                editorComponent.remove(panel)
                            }


                        val backgroundPanel = BackgroundPanel(image)
                        backgroundPanel.updateSize(editorComponent.width, editorComponent.height)

                        editorComponent.add(backgroundPanel)
                        editorComponent.revalidate()
                        editorComponent.repaint()
                    }


                    val settings = BackgroundImageSettings.getInstance()
                    settings?.let {
                        it.state.backgroundImagePath = imagePath
                        Messages.showMessageDialog("Background image updated!", "Success", Messages.getInformationIcon())
                    } ?: run {
                        Messages.showMessageDialog("Failed to get BackgroundImageSettings instance.", "Error", Messages.getErrorIcon())
                    }
                }
            } else {
                Messages.showMessageDialog("Image file not found: ${file.absolutePath}", "Error", Messages.getErrorIcon())
            }
        }
    }
}
