package org.charon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.ui.Messages
import java.awt.Color
import javax.swing.JComponent

class SetTransparentBackgroundAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

        val editors = EditorFactory.getInstance().allEditors
        if (editors.isEmpty()) {
            Messages.showMessageDialog("No open editors found.", "Error", Messages.getErrorIcon())
            return
        }

        for (editor in editors) {
            val editorComponent = editor.component


            setTransparency(editorComponent, 0.5f)

            editorComponent.revalidate()
            editorComponent.repaint()
        }

    }

    private fun setTransparency(component: JComponent, opacity: Float) {
        val bgColor = Color(0, 0, 0, (255 * opacity).toInt())
        component.background = bgColor
        component.isOpaque = false

        for (child in component.components) {
            if (child is JComponent) {
                setTransparency(child, opacity)
            }
        }
    }
}
