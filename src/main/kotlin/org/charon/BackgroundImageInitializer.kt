package org.charon

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.util.messages.MessageBusConnection
import javax.swing.ImageIcon
import javax.swing.JLayeredPane
import java.awt.Image

class BackgroundImageInitializer : ProjectActivity {

    override suspend fun execute(project: Project) {
        com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater {
            applyBackgroundToAllOpenEditors(project)
        }

        val connection = project.messageBus.connect()
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, object : FileEditorManagerListener {
            override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
                com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater {
                    applyBackgroundToAllOpenEditors(project)
                }
            }
        })
    }

    private fun applyBackgroundToAllOpenEditors(project: Project) {
        val settings = BackgroundImageSettings.getInstance()
        val imagePath = settings.state.backgroundImagePath ?: return

        val imageIcon = ImageIcon(imagePath)
        val image = imageIcon.image

        val editors = EditorFactory.getInstance().allEditors
        for (editor in editors) {
            applyBackground(editor, image)
        }
    }

    private fun applyBackground(editor: Editor, image: Image) {
        val editorComponent = editor.component

        com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater {
            editorComponent.components
                .filterIsInstance<BackgroundPanel>()
                .forEach { panel ->
                    editorComponent.remove(panel)
                }

            val backgroundPanel = BackgroundPanel(image)
            backgroundPanel.setBounds(0, 0, editorComponent.width, editorComponent.height)

            editorComponent.add(backgroundPanel)
            editorComponent.revalidate()
            editorComponent.repaint()
        }
    }
}

