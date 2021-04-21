package ru.maximus.old48.screens

import com.badlogic.gdx.Gdx
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

class MainMenuScreen(val context: Context): KtxScreen {

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
            "LOAD \"Project D\"\n" +
            "\n" +
            "PRESS PLAY ON TAPE\n" +
            "LOADING...\n" +
            "READY.\n" +
            "RUN"
    val runLabelDuration = 0.2f
    var runLabelTimer = 0f
    val runLabel: Label

    // Info window
    var infoWindowTimer = 0f
    val infoWindowDuration = 0.7f
    val infoWindow: Window
    // Leaderboard window
    var leaderWindowTimer = 0f
    val leaderWindowDuration = 0.8f
    val leaderWindow: Window
    // Start window
    var startWindowTimer = 0f
    val startWindowDuration = 0.9f
    val startWindow: Window

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

            startWindow = window("") {
                setSize(300f, 172f)
                setPosition(250f, 50f)

                label("**** NEW GAME ****")
                row()
                label("")
                row()
                table {
                    button("sound")
                    slider().apply {
                        value = 1f
                    }
                    row()
                    button("music")
                    slider().apply {
                        value = 1f
                    }
                }
                row()
                textButton("START").apply {
                    onChange {
                        setScreen<GameScreen>(context)
                    }
                }
            }
            leaderWindow = window("", "dialog") {
                titleTable.apply {
                    reset()
                    add(label("LEADERBOARD", "title"))
                }
                setSize(250f, 215f)
                setPosition(350f, 250f)

                table {
                    for (i in 1..10) {
                        label("$i. ")
                        label("Player")
                        row()
                    }
                }
            }
            infoWindow = window("", "dialog") {
                titleTable.apply {
                    reset()
                    add(label("INDORMATION", "title"))
                }
                setSize(350f, 215f)
                setPosition(650f, 140f)

                table {
                    label("Game:\n  Project D")
                    row()
                    label("Made by team:\n  Shrek is love\n  Shrek is live")
                    row()
                    textButton("55_maximus_55").apply {
                        onChange {
                            Gdx.net.openURI("http://vk.com/55maximus55")
                        }
                    }
                }
            }
        }
    }

    override fun render(delta: Float) {
        systemLabel.setText("**** ${systemLabelText.subSequence(0, ((systemLabelTimer / systemLabelDuration) * systemLabelText.length).toInt())} ****")
        memoryLabel.setText("${memoryLabelText.subSequence(0, ((memoryLabelTimer / memoryLabelDuration) * memoryLabelText.length).toInt())}")
        runLabel.setText("${runLabelText.subSequence(0, ((runLabelTimer / runLabelDuration) * runLabelText.length).toInt())}")
        if (!leaderWindow.isVisible && leaderWindowTimer > leaderWindowDuration) leaderWindow.isVisible = true
        if (!startWindow.isVisible && startWindowTimer > startWindowDuration) startWindow.isVisible = true
        if (!infoWindow.isVisible && infoWindowTimer > infoWindowDuration) infoWindow.isVisible = true

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

        infoWindowTimer += delta
        leaderWindowTimer += delta
        startWindowTimer += delta
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage

        systemLabelTimer = 0f
        memoryLabelTimer = 0f
        runLabelTimer = 0f

        leaderWindowTimer = 0f
        leaderWindow.isVisible = false
        startWindowTimer = 0f
        startWindow.isVisible = false
        infoWindowTimer = 0f
        infoWindow.isVisible = false
    }
    override fun hide() {
        Gdx.input.inputProcessor = null
    }
}