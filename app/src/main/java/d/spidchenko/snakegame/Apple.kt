package d.spidchenko.snakegame

import android.content.Context
import android.graphics.*
import kotlin.random.Random
import kotlin.random.nextInt

class Apple(
    context: Context,
    private val spawnRange: Point,
    private val size: Int
) {
    private var locationOnGrid = Point(-10, 0)
    private val bitmap: Bitmap

    init {
        val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.apple)
        bitmap = Bitmap.createScaledBitmap(originalBitmap, size, size, true)
    }

    fun spawn() {
        locationOnGrid.x = Random.nextInt(1..spawnRange.x)
        locationOnGrid.y = Random.nextInt(0 until spawnRange.y)
    }

    fun getLocation() = locationOnGrid

    fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(
            bitmap,
            locationOnGrid.x * size.toFloat(),
            locationOnGrid.y * size.toFloat(),
            paint
        )
    }
}
