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

    private lateinit var mGameThread: Thread
    private var mNextFrameTime = 0L
    private var mPlaying = false
    private var mPaused = true
    private var mSceneInitialized = false
    private var mScreenX: Int = 0
    private var mScreenY: Int = 0
    private var mBlockSize = 0

    private val mSurfaceHolder: SurfaceHolder = holder
    private lateinit var mCanvas: Canvas
    private val mPaint: Paint = Paint()

    private lateinit var mSoundPool: SoundPool
    private var mEatId: Int = 0
    private var mCrashId: Int = 0

    private var mNumBlocksHigh = 0
    private var mScore = 0
    private lateinit var mSnake: Snake
    private lateinit var mApple: Apple

    fun resume() {
        Log.d(TAG, "resume")
        mPlaying = true
        // TODO Need to use Kotlin coroutines instead of Threads
        mGameThread = Thread(this)
        mGameThread.start()
    }

    override fun run() {
        while (mPlaying) {
            if (!mPaused) {
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
        mPlaying = false
        mGameThread.join()
    }

    private fun update() {
        // Move the snake
        // Did the head of the snake eat the apple?
        // Did the snake die?
    }

    private fun updateRequired(): Boolean {
        // TODO I think coroutine will be great here
        if (mNextFrameTime <= System.currentTimeMillis()) {
            mNextFrameTime = System.currentTimeMillis() + MILLS_PER_SECOND / TARGET_FPS
            return true
        }
        return false
    }

    private fun draw() {
        if (holder.surface.isValid) {
            if (!mSceneInitialized) {
                initialize2D()
                initializeAudio()
                mSceneInitialized = true
            }
            mCanvas = mSurfaceHolder.lockCanvas()
            mCanvas.drawColor(Color.argb(255, 26, 128, 182))
            mPaint.color = Color.WHITE
            mPaint.textSize = 120F

            mCanvas.drawText("" + mScore, 20F, 120F, mPaint);
            mApple.draw(mCanvas, mPaint)

            if (mPaused) {
                mPaint.color = Color.WHITE
                mPaint.textSize = 250F
                mCanvas.drawText(resources.getString(R.string.tap_to_play), 200F, 700F, mPaint)
            }
            mSurfaceHolder.unlockCanvasAndPost(mCanvas)
        }
    }

    private fun initialize2D() {
        mScreenX = mSurfaceHolder.surfaceFrame.width()
        mScreenY = mSurfaceHolder.surfaceFrame.height()
        mBlockSize = mScreenX / NUM_BLOCKS_WIDE
        mNumBlocksHigh = mScreenY / mBlockSize

        mApple = Apple(context, Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), mBlockSize)
    }

    private fun initializeAudio() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        mSoundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        try {
            val assetsManager = context.assets
            var fileDescriptor = assetsManager.openFd("get_apple.ogg")
            mEatId = mSoundPool.load(fileDescriptor, 0)
            fileDescriptor = assetsManager.openFd("snake_death.ogg")
            mCrashId = mSoundPool.load(fileDescriptor, 0)

        } catch (e: IOException) {
            Log.d(TAG, "initializeAudio: Error: $e")
        }
    }

    private fun startNewGame() {
        // reset the snake

        mApple.spawn()

        // Reset the mScore
        mScore = 0;
        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action.and(MotionEvent.ACTION_MASK)) {

                MotionEvent.ACTION_DOWN -> {

                }

                MotionEvent.ACTION_UP -> {
                    if (mPaused) {
                        mPaused = false
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
