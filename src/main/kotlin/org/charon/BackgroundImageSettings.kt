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
        var backgroundImagePath: String? = null
    )

    companion object {
        fun getInstance(): BackgroundImageSettings {
            return com.intellij.openapi.components.ServiceManager.getService(BackgroundImageSettings::class.java)
        }
    }
}
