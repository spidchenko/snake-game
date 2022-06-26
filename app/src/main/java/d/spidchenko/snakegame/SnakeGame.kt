package d.spidchenko.snakegame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class SnakeGame(context: Context) : SurfaceView(context), Runnable {

    private lateinit var gameThread: Thread
    private var nextFrameTime = 0L
    private var isPlaying = false
    private var isPaused = true
    private var isSceneInitialized = false
    private var screenX: Int = 0
    private var screenY: Int = 0
    private var blockSize = 0

    private val surfaceHolder: SurfaceHolder = holder
    private lateinit var canvas: Canvas
    private val paint: Paint = Paint()

    private lateinit var soundPool: SoundPool
    private var eatId: Int = 0
    private var crashId: Int = 0

    private var numBlocksHigh = 0
    private var score = 0
    private lateinit var snake: Snake
    private lateinit var apple: Apple

    fun resume() {
        Log.d(TAG, "resume")
        isPlaying = true
        // TODO Need to use Kotlin coroutines instead of Threads
        gameThread = Thread(this)
        gameThread.start()
    }

    override fun run() {
        while (isPlaying) {
            if (!isPaused) {
                // Update 10 times a second
                if (updateRequired()) {
                    update()
                }
            }
            draw()
        }
    }

    fun pause() {
        Log.d(TAG, "pause")
        isPlaying = false
        gameThread.join()
    }

    private fun update() {
        // Move the snake
        // Did the head of the snake eat the apple?
        // Did the snake die?
    }

    private fun updateRequired(): Boolean {
        // TODO I think coroutine will be great here
        if (nextFrameTime <= System.currentTimeMillis()) {
            nextFrameTime = System.currentTimeMillis() + MILLS_PER_SECOND / TARGET_FPS
            return true
        }
        return false
    }

    private fun draw() {
        if (holder.surface.isValid) {
            if (!isSceneInitialized) {
                initialize2D()
                initializeAudio()
                isSceneInitialized = true
            }
            canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.argb(255, 26, 128, 182))
            paint.color = Color.WHITE
            paint.textSize = 120F

            canvas.drawText("" + score, 20F, 120F, paint)
            apple.draw(canvas, paint)

            if (isPaused) {
                paint.color = Color.WHITE
                paint.textSize = 250F
                canvas.drawText(resources.getString(R.string.tap_to_play), 200F, 700F, paint)
            }
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun initialize2D() {
        screenX = surfaceHolder.surfaceFrame.width()
        screenY = surfaceHolder.surfaceFrame.height()
        blockSize = screenX / NUM_BLOCKS_WIDE
        numBlocksHigh = screenY / blockSize

        apple = Apple(context, Point(NUM_BLOCKS_WIDE, numBlocksHigh), blockSize)
    }

    private fun initializeAudio() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        try {
            val assetsManager = context.assets
            var fileDescriptor = assetsManager.openFd("get_apple.ogg")
            eatId = soundPool.load(fileDescriptor, 0)
            fileDescriptor = assetsManager.openFd("snake_death.ogg")
            crashId = soundPool.load(fileDescriptor, 0)

        } catch (e: IOException) {
            Log.d(TAG, "initializeAudio: Error: $e")
        }
    }

    private fun startNewGame() {
        // reset the snake

        apple.spawn()

        // Reset the mScore
        score = 0
        // Setup mNextFrameTime so an update can triggered
        nextFrameTime = System.currentTimeMillis()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action.and(MotionEvent.ACTION_MASK)) {

                MotionEvent.ACTION_DOWN -> {

                }

                MotionEvent.ACTION_UP -> {
                    if (isPaused) {
                        isPaused = false
                        startNewGame()
                    }
                }
            }
        }
        return true
    }

    companion object {
        private const val TAG = "SnakeGame.LOG_TAG"
        const val NUM_BLOCKS_WIDE = 10
        const val TARGET_FPS = 10
        const val MILLS_PER_SECOND = 1000L
    }
}
