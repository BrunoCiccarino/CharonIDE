package org.charon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.ui.Messages
import javax.swing.ImageIcon

class ToggleBackgroundAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val settings = BackgroundImageSettings.getInstance()
        var currentIndex = settings.state.selectedBackgroundIndex

        // Se for a primeira vez, defina o plano de fundo inicial
        if (currentIndex == -1) {
            currentIndex = 0
            settings.state.selectedBackgroundIndex = currentIndex
        } else {
            currentIndex = BackgroundImageSettings.getNextBackgroundIndex(currentIndex)
            settings.state.selectedBackgroundIndex = currentIndex
        }

        com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater {
            val editors = EditorFactory.getInstance().allEditors
            if (editors.isNotEmpty()) {
                val imagePath = BackgroundImageSettings.getBackgroundImagePath(currentIndex)
                val imageUrl = javaClass.getResource(imagePath)

                if (imageUrl != null) {
                    val imageIcon = ImageIcon(imageUrl)
                    val image = imageIcon.image

                    for (editor in editors) {
                        val editorComponent = editor.component

                        // Remover painéis de fundo existentes
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
                } else {
                    Messages.showErrorDialog(
                        "Background image not found: $imagePath",
                        "Error Loading Image"
                    )
                }
            }
        }
    }
}

