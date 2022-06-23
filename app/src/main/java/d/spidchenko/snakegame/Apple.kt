package d.spidchenko.snakegame

import android.content.Context
import android.graphics.*
import kotlin.random.Random
import kotlin.random.nextInt

class Apple(
    private val mContext: Context,
    private val mSpawnRange: Point,
    private val mSize: Int
) {
    private var mLocationOnGrid = Point(-10, 0)
    private val mBitmap: Bitmap

    init {
        val bitmap = BitmapFactory.decodeResource(mContext.resources, R.drawable.apple)
        mBitmap = Bitmap.createScaledBitmap(bitmap, mSize, mSize, true) //bitmap.scale(mSize, mSize)
    }

    fun spawn() {
        mLocationOnGrid.x = Random.nextInt(1..mSpawnRange.x)
        mLocationOnGrid.y = Random.nextInt(0 until mSpawnRange.y)
    }

    fun getLocation() = mLocationOnGrid

    fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(
            mBitmap,
            mLocationOnGrid.x * mSize.toFloat(),
            mLocationOnGrid.y * mSize.toFloat(),
            paint
        )
    }
}
