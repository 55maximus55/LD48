package ru.maximus.old48

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.app.KtxGame
import ktx.assets.toInternalFile
import ktx.assets.toLocalFile
import ktx.graphics.takeScreenshot
import ktx.inject.Context
import ktx.inject.register
import ktx.scene2d.Scene2DSkin
import ru.maximus.old48.screens.GameScreen
import ru.maximus.old48.screens.MainMenuScreen
import java.time.LocalDateTime

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Main : KtxGame<Screen>() {
    val context = Context()

    override fun create() {
        context.register {
            bindSingleton(this@Main)
            bindSingleton(SpriteBatch())
            bindSingleton(ShapeRenderer())
        }
        Scene2DSkin.defaultSkin = Skin("skins/commodore64/uiskin.json".toInternalFile())

        context.register {
            bindSingleton(MainMenuScreen(context))
            bindSingleton(GameScreen(context))
        }
        addScreen(context.inject<MainMenuScreen>())
        addScreen(context.inject<GameScreen>())

        // TODO: set to Main Menu
        setScreen<MainMenuScreen>()
        setScreen<GameScreen>()
    }

    override fun render() {
        super.render()
        // TODO: set to Main Menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
            setScreen<GameScreen>(context)
        if (Gdx.input.isKeyJustPressed(Input.Keys.F12))
            takeScreenshot("screenshots/${LocalDateTime.now().second}-${LocalDateTime.now().minute}-${LocalDateTime.now().hour}-${LocalDateTime.now().dayOfMonth}-${LocalDateTime.now().monthValue}-${LocalDateTime.now().year}.png".toLocalFile())
    }

    private var disposed = false
    override fun dispose() {
        disposed = true
        if (!disposed)
            context.dispose()
    }
}

inline fun <reified T : Screen> setScreen(context: Context) {
    context.inject<Main>().setScreen<T>()
}