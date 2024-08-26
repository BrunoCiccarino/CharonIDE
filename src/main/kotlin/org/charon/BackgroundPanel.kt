package org.charon

import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import javax.swing.JPanel

class BackgroundPanel(private val backgroundImage: Image) : JPanel() {

    init {
        isOpaque = false
        }
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.drawImage(backgroundImage, 0, 0, width, height, this)
    }
}
