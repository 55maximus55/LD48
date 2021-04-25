package ru.maximus.old48.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Version
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.SharedLibraryLoader
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.actors.onChange
import ktx.actors.stage
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.scene2d.*
import ru.maximus.old48.setScreen

class GameOverScreen(val context: Context) : KtxScreen {

    val projectName = "CyberAssHacker"
    // System label
    val systemLabelText = "${
        when {
            SharedLibraryLoader.isWindows -> "WINDOWS"
            SharedLibraryLoader.isLinux -> "LINUX"
            SharedLibraryLoader.isMac -> "MAC"
            else -> "ERROR"
        }
    } ${if (SharedLibraryLoader.is64Bit) "64" else "32"} LibGDX V${Version.VERSION}"
    val systemLabelDuration = 0.2f
    var systemLabelTimer = 0f
    val systemLabel: Label
    // Memory label
    val memoryLabelText = "64M RAM SYSTEM 38911 BASIC BYTES FREE"
    val memoryLabelDuration = 0.2f
    var memoryLabelTimer = 0f
    val memoryLabel: Label
    // Run label
    val runLabelText = "\n" +
            "\n" +
            "\n" +
            "\n" +
            "READY.\n" +
            "LOAD \"$projectName\"\n" +
            "\n" +
            "PRESS PLAY ON TAPE\n" +
            "LOADING...\n" +
            "READY.\n" +
            "RUN\n" +
            "\n" +
            "GAME OVER!\n" +
            "You need to go deeper!\n" +
            "\n" +
            "PRESS SPACE TO CONTINUE"
    val runLabelDuration = 0.2f
    var runLabelTimer = 0f
    val runLabel: Label

    val batch: SpriteBatch = context.inject()
    val stage = stage(batch, ScreenViewport()).apply {
        actors {
            table {
                background("window")
                setFillParent(true)
            }
            table {
                setFillParent(true)
                top()
                pad(22f)

                systemLabel = label("****  ****")
                row()
                memoryLabel = label("")
            }
            table {
                setFillParent(true)
                top()
                left()
                pad(22f)

                runLabel = label("")
            }
        }
    }

    override fun render(delta: Float) {
        systemLabel.setText("**** ${systemLabelText.subSequence(0, ((systemLabelTimer / systemLabelDuration) * systemLabelText.length).toInt())} ****")
        memoryLabel.setText("${memoryLabelText.subSequence(0, ((memoryLabelTimer / memoryLabelDuration) * memoryLabelText.length).toInt())}")
        runLabel.setText("${runLabelText.subSequence(0, ((runLabelTimer / runLabelDuration) * runLabelText.length).toInt())}")

        stage.apply {
            act()
            draw()
        }

        systemLabelTimer += delta
        if (systemLabelTimer > systemLabelDuration) {
            systemLabelTimer = systemLabelDuration
            memoryLabelTimer += delta
            if (memoryLabelTimer > memoryLabelDuration) {
                memoryLabelTimer = memoryLabelDuration
                runLabelTimer += delta
                if (runLabelTimer > runLabelDuration) {
                    runLabelTimer = runLabelDuration
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            setScreen<MainMenuScreen>(context)
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage

        systemLabelTimer = 0f
        memoryLabelTimer = 0f
        runLabelTimer = 0f
    }
    override fun hide() {
        Gdx.input.inputProcessor = null
    }

}