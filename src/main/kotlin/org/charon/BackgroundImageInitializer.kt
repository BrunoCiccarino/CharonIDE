package org.charon

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.VirtualFile
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

        val layeredPane = editorComponent.components
            .filterIsInstance<JLayeredPane>()
            .firstOrNull() ?: JLayeredPane().apply {
            setBounds(0, 0, editorComponent.width, editorComponent.height)
            editorComponent.add(this)
            editorComponent.revalidate()
            editorComponent.repaint()
        }

        layeredPane.components
            .filterIsInstance<BackgroundPanel>()
            .forEach { panel ->
                layeredPane.remove(panel)
            }

        val backgroundPanel = BackgroundPanel(image).apply {
            setBounds(0, 0, editorComponent.width, editorComponent.height)
            addComponentListener(object : java.awt.event.ComponentAdapter() {
                override fun componentResized(e: java.awt.event.ComponentEvent) {
                    updateSize(editorComponent.width, editorComponent.height)
                }
            })
        }

        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER)

        // Ensure the layered pane and editor component are updated correctly
        layeredPane.revalidate()
        layeredPane.repaint()
    }
}
