package ru.maximus.old48.screens

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.inject.Context
import ru.maximus.old48.Timer

class GameScreen(val context: Context) : KtxScreen {

    val shapeRenderer: ShapeRenderer = context.inject()
    val batch: SpriteBatch = context.inject()
    val assetManager: AssetManager = context.inject()

    val skyHeight = 280f
    val cellWidth = 30f
    val cellPerspective = 3.6f
    val cellHeight = 45f
    var speed = 2f

    var initX = ((320f / cellWidth).toInt() + 1) - 1

    override fun show() {
        skyTimer.clear()
        verticalTimer.clear()
        carSpawnTimer1.clear()
        carSpawnTimer2.clear()

        angles.clear()
        initX = -((320f / cellWidth).toInt() + 1)
        horTimer = 0f
    }

    val skyTimer = Timer(start = 0.3f, end = 0.7f)
    val verticalTimer = Timer(start = 0.8f, end = 1.3f)
    val angles = ArrayList<Float>()

    var horTimer = 0f
    val horStart = 1.4f

    val carSpawnTimer1 = Timer(start = 1.9f, end = 2.2f)
    val carSpawnTimer2 = Timer(start = 2.2f, end = 2.5f)



    val playerTexture: Texture = assetManager["player.png"]
    val playerTextureRegion = TextureRegion(playerTexture)

    override fun render(delta: Float) {
        skyTimer.update(delta)
        verticalTimer.update(delta)
        carSpawnTimer1.update(delta)
        carSpawnTimer2.update(delta)

        horTimer += delta

        shapeRenderer.apply {
            /* Grid render */
            use(ShapeRenderer.ShapeType.Filled) {
                color = Color.PURPLE
                rectLine(
                    320f - skyTimer.value() * 320f, skyHeight,
                    320f + skyTimer.value() * 320f, skyHeight,
                    2f
                )
            }
            use(ShapeRenderer.ShapeType.Line) {
                color = Color.PURPLE
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
            }
        }

        batch.apply {
            use {
                val spriteSize = 96
                playerTextureRegion.setRegion(spriteSize * 4, 0, spriteSize, spriteSize)
                if (carSpawnTimer2.value() > 0f) {
                    draw(playerTextureRegion, 320f - spriteSize / 2f, 10f)
                }
            }
        }

        shapeRenderer.apply {
            use(ShapeRenderer.ShapeType.Filled) {
                val spriteSize = 96f
                color = Color.GREEN
                rect(320f - spriteSize / 2f, 10f + spriteSize * carSpawnTimer2.value(), spriteSize, spriteSize * (carSpawnTimer1.value() - carSpawnTimer2.value()))
            }
        }
    }

}