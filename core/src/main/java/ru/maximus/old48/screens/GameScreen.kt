package ru.maximus.old48.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.inject.Context
import ru.maximus.old48.Timer

class GameScreen(val context: Context) : KtxScreen {

    val shapeRenderer: ShapeRenderer = context.inject()

    val skyHeight = 280f
    val cellWidth = 30f
    val cellPerspective = 3.6f
    val cellHeight = 30f
    var speed = 3f

    var initX = ((320f / cellWidth).toInt() + 1) - 1

    override fun show() {
        shapeRenderer.color = Color.PURPLE

        for (i in angles)
            println(i)

        skyTimer.clear()
        verticalTimer.clear()
        angles.clear()
        initX = -((320f / cellWidth).toInt() + 1)
        horTimer = 0f
    }

    val skyTimer = Timer(start = 0.3f, end = 0.7f)
    val verticalTimer = Timer(start = 0.8f, end = 1.3f)
    val angles = ArrayList<Float>()

    var horTimer = 0f
    val horStart = 1.4f

    override fun render(delta: Float) {
        skyTimer.update(delta)
        verticalTimer.update(delta)

        horTimer += delta

        shapeRenderer.apply {
            /* Grid render */
            use(ShapeRenderer.ShapeType.Line) {
                rectLine(
                    320f - skyTimer.value() * 320f, skyHeight,
                    320f + skyTimer.value() * 320f, skyHeight,
                    2f
                )
                for (x in 0 until (320f / cellWidth).toInt() + 1) {
                    line(
                        320f + (cellWidth / 2 + x * cellWidth),
                        skyHeight,
                        (320f + (cellWidth / 2 + x * cellWidth)) + ((cellWidth / 2 + x * cellWidth) * cellPerspective) * verticalTimer.value(),
                        skyHeight - skyHeight * verticalTimer.value()
                    )
                    line(
                        320f - (cellWidth / 2 + x * cellWidth),
                        skyHeight,
                        (320f - (cellWidth / 2 + x * cellWidth)) - ((cellWidth / 2 + x * cellWidth) * cellPerspective) * verticalTimer.value(),
                        skyHeight - skyHeight * verticalTimer.value()
                    )
                }
                /* Horizontal lines */
                if (horTimer > horStart) {
                    var height = skyHeight * (1f - (horTimer - horStart) / speed)
                    var y = 0
                    while (skyHeight > height + y * cellHeight) {
                        line(0f, height + cellHeight * y, 640f, height + cellHeight * y)
                        y++
                    }
                    if (height < 0f) height += cellHeight
                }

                /* Angles */
                color = Color.RED
                initX++
                when {
                    initX < 0 -> {
                        line(
                            320f, 357.777777f,
                            (320f - (cellWidth / 2 - initX * cellWidth)) - ((cellWidth / 2 - initX * cellWidth) * cellPerspective),
                            0f
                        )
                        angles.add(
                            Vector2(
                                (320f - (cellWidth / 2 - initX * cellWidth)) - ((cellWidth / 2 - initX * cellWidth) * cellPerspective) - 320f,
                                -357.777777f
                            ).angleDeg()
                        )
                    }
                    initX == 0 -> {
                        line(
                            320f, 357.777777f,
                            (320f - (cellWidth / 2 + initX * cellWidth)) - ((cellWidth / 2 + initX * cellWidth) * cellPerspective),
                            0f
                        )
                        line(
                            320f, 357.777777f,
                            (320f + (cellWidth / 2 + initX * cellWidth)) + ((cellWidth / 2 + initX * cellWidth) * cellPerspective),
                            0f
                        )

                        Vector2(
                            (320f - (cellWidth / 2 - initX * cellWidth)) - ((cellWidth / 2 - initX * cellWidth) * cellPerspective) - 320f,
                            -357.777777f
                        ).angleDeg()
                        Vector2(
                            (320f + (cellWidth / 2 + initX * cellWidth)) + ((cellWidth / 2 + initX * cellWidth) * cellPerspective) - 320f,
                            -357.777777f
                        ).angleDeg()
                    }
                    initX > 0 && initX <= ((320f / cellWidth).toInt()) -> {
                        line(
                            320f, 357.777777f,
                            (320f + (cellWidth / 2 + initX * cellWidth)) + ((cellWidth / 2 + initX * cellWidth) * cellPerspective),
                            0f
                        )
                        angles.add(
                            Vector2(
                                (320f + (cellWidth / 2 + initX * cellWidth)) + ((cellWidth / 2 + initX * cellWidth) * cellPerspective) - 320f,
                                -357.777777f
                            ).angleDeg()
                        )
                    }
                }

                color = Color.PURPLE
            }
        }
    }

}