package d.spidchenko.snakegame

import android.content.Context
import android.graphics.*
import android.view.MotionEvent

class Snake(
    context: Context,
    private val moveRange: Point,
    private val segmentSize: Int
) {
    private enum class Heading {
        UP, RIGHT, DOWN, LEFT
    }

    private val halfWayPoint: Int
    private val segmentLocations: ArrayList<Point> = ArrayList()
    private var heading = Heading.RIGHT

    private val bitmapHeadRight: Bitmap
    private val bitmapHeadLeft: Bitmap
    private val bitmapHeadUp: Bitmap
    private val bitmapHeadDown: Bitmap
    private val bitmapBody: Bitmap

    init {
        val originalHeadBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.head)
        val originalBodyBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.body)
        bitmapHeadRight =
            Bitmap.createScaledBitmap(originalHeadBitmap, segmentSize, segmentSize, true)
        val matrix = Matrix()
        matrix.preScale(-1F, -1F)

        bitmapHeadLeft =
            Bitmap.createBitmap(bitmapHeadRight, 0, 0, segmentSize, segmentSize, matrix, true)

        matrix.preRotate(-90F)

        bitmapHeadDown =
            Bitmap.createBitmap(bitmapHeadRight, 0, 0, segmentSize, segmentSize, matrix, true)

        matrix.preRotate(180F)

        bitmapHeadUp =
            Bitmap.createBitmap(bitmapHeadRight, 0, 0, segmentSize, segmentSize, matrix, true)

        bitmapBody = Bitmap.createScaledBitmap(originalBodyBitmap, segmentSize, segmentSize, true)

        halfWayPoint = moveRange.x * segmentSize / 2
    }

    fun reset() {
        heading = Heading.RIGHT
        segmentLocations.clear()
        segmentLocations.add(Point(moveRange.x / 2, moveRange.y / 2))
    }

    fun move() {
        for (i in segmentLocations.lastIndex downTo 1) {
            segmentLocations[i].x = segmentLocations[i - 1].x
            segmentLocations[i].y = segmentLocations[i - 1].y
        }

        val head: Point = segmentLocations[0]
        when (heading) {
            Heading.UP -> head.y--
            Heading.DOWN -> head.y++
            Heading.LEFT -> head.x--
            Heading.RIGHT -> head.x++
        }
        segmentLocations[0] = head
    }

    fun detectDeath(): Boolean {
        var isDead = false
        val head: Point = segmentLocations[0]

        if (head.x == -1 || head.x > moveRange.x ||
            head.y == -1 || head.y > moveRange.y
        ) {
            isDead = true
        }

        for (i in segmentLocations.lastIndex downTo 1) {
            if (head.x == segmentLocations[i].x && head.y == segmentLocations[i].y) {
                isDead = true
            }
        }
        return isDead
    }

    fun checkDinner(apple: Point): Boolean {
        val head: Point = segmentLocations[0]
        if (head.x == apple.x && head.y == apple.y) {
            segmentLocations.add(Point(-10, -10))
            return true
        }
        return false
    }

    fun draw(canvas: Canvas, paint: Paint) {
        if (segmentLocations.isNotEmpty()) {
            val head = segmentLocations[0]
            val headLeft = head.x * segmentSize.toFloat()
            val headTop = head.y * segmentSize.toFloat()
            val bitmapToDraw = when (heading) {
                Heading.RIGHT -> bitmapHeadRight
                Heading.LEFT -> bitmapHeadLeft
                Heading.UP -> bitmapHeadUp
                Heading.DOWN -> bitmapHeadDown
            }
            canvas.drawBitmap(bitmapToDraw, headLeft, headTop, paint)

            for (i in 1..segmentLocations.lastIndex) {
                val segmentLeft = segmentLocations[i].x * segmentSize.toFloat()
                val segmentTop = segmentLocations[i].y * segmentSize.toFloat()
                canvas.drawBitmap(bitmapBody, segmentLeft, segmentTop, paint)
            }
        }
    }

    fun switchHeading(motionEvent: MotionEvent) =
        if (motionEvent.x > halfWayPoint) rotateRight() else rotateLeft()

    private fun rotateRight() {
        heading = when (heading) {
            Heading.UP -> Heading.RIGHT
            Heading.RIGHT -> Heading.DOWN
            Heading.DOWN -> Heading.LEFT
            Heading.LEFT -> Heading.UP
        }
    }

    private fun rotateLeft() {
        heading = when (heading) {
            Heading.UP -> Heading.LEFT
            Heading.LEFT -> Heading.DOWN
            Heading.DOWN -> Heading.RIGHT
            Heading.RIGHT -> Heading.UP
        }
    }
}
