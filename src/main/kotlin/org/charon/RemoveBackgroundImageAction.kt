package org.charon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.ui.Messages
import java.awt.Color
import javax.swing.JComponent

class SetTransparentBackgroundAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        // Obtém todos os editores abertos
        val editors = EditorFactory.getInstance().allEditors
        if (editors.isEmpty()) {
            Messages.showMessageDialog("No open editors found.", "Error", Messages.getErrorIcon())
            return
        }

        for (editor in editors) {
            val editorComponent = editor.component

            // Configura a opacidade para permitir a visualização do fundo
            setTransparency(editorComponent, 0.5f)

            // Revalida e repinta o componente do editor
            editorComponent.revalidate()
            editorComponent.repaint()
        }

        Messages.showMessageDialog("Editor background set to transparent!", "Success", Messages.getInformationIcon())
    }

    private fun setTransparency(component: JComponent, opacity: Float) {
        // Define a cor do fundo com o nível de transparência desejado
        val bgColor = Color(0, 0, 0, (255 * opacity).toInt())
        component.background = bgColor
        component.isOpaque = false

        // Aplica a transparência a todos os componentes filhos
        for (child in component.components) {
            if (child is JComponent) {
                setTransparency(child, opacity)
            }
        }
    }
}
