package org.charon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorSettings
import javax.swing.ImageIcon
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

class SetBackgroundImageAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return

        // Show file dialog to select an image
        val frame = Frame()
        val fileDialog = FileDialog(frame, "Select Background Image", FileDialog.LOAD)
        fileDialog.isVisible = true

        val filePath = fileDialog.file?.let { "${fileDialog.directory}/$it" }

        if (filePath != null) {
            // Load the image
            val file = File(filePath)
            if (file.exists()) {
                val imageIcon = ImageIcon(file.absolutePath)
                val image = imageIcon.image

                // Get the editor component
                val editor = EditorFactory.getInstance().allEditors.firstOrNull() ?: return
                val editorComponent = editor.component

                // Remove any existing background panel
                editorComponent.components.filterIsInstance<BackgroundPanel>().forEach {
                    editorComponent.remove(it)
                }

                // Add the new background panel
                val backgroundPanel = BackgroundPanel(image)
                editorComponent.add(backgroundPanel)
                editorComponent.revalidate()
                editorComponent.repaint()

                Messages.showMessageDialog("Background image updated!", "Success", Messages.getInformationIcon())
            } else {
                Messages.showMessageDialog("Image file not found: $filePath", "Error", Messages.getErrorIcon())
            }
        }
    }
}
