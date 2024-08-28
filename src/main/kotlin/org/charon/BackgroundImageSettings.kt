package org.charon

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "BackgroundImageSettings",
    storages = [Storage("BackgroundImageSettings.xml")]
)
class BackgroundImageSettings : PersistentStateComponent<BackgroundImageSettings.State> {

    var myState: State = State()

    override fun getState(): State {
        return myState
    }

    override fun loadState(myState: State) {
        this.myState = myState
    }

    data class State(
        var selectedBackgroundIndex: Int = 0
    )

    companion object {
        private val availableBackgrounds = arrayOf(
            "/resources/backgrounds/bg1.png",
            "/resources/backgrounds/bg2.png",
            "/resources/backgrounds/bg3.png"
        )

        fun getInstance(): BackgroundImageSettings {
            return com.intellij.openapi.components.ServiceManager.getService(BackgroundImageSettings::class.java)
        }

        fun getBackgroundImagePath(index: Int): String {
            return availableBackgrounds.getOrNull(index) ?: availableBackgrounds.first()
        }

        fun getNextBackgroundIndex(currentIndex: Int): Int {
            return (currentIndex + 1) % availableBackgrounds.size
        }
    }
}

