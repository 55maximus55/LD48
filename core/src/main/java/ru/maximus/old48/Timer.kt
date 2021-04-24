package ru.maximus.old48

class Timer(val start: Float, val end: Float) {

    var timer = 0f

    fun update(delta: Float) {
        timer += delta
    }

    fun clear() {
        timer = 0f
    }

    fun value(): Float {
        return if (timer < start) 0f else if (timer > end) 1f else (timer - start) / (end - start)
    }

}