package d.spidchenko.snakegame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.media.SoundPool
import android.view.SurfaceHolder
import android.view.SurfaceView

class SnakeGame(context: Context) : SurfaceView(context) {

    private lateinit var mGameThread: Thread
    private var mNextFrameTime = 0L
    private var mPlaying = false
    private var mPaused = true
    private var mSceneInitialized = false

    private val mSurfaceHolder: SurfaceHolder = holder
    private lateinit var mCanvas: Canvas
    private val mPaint: Paint = Paint()

    private lateinit var mSoundPool: SoundPool
    private var mEatId: Int = 0
    private var mCrashId: Int = 0

    private var mNumBlocksHigh = 0
    private var mScore = 0
    private val mSnake = Snake()
    private val mApple = Apple()


    fun resume() {
        TODO("Not yet implemented")
    }

    fun pause() {
        TODO("Not yet implemented")
    }

    companion object{
        private const val TAG = "SnakeGame.LOG_TAG"
        const val NUM_BLOCKS_WIDE = 10
    }
}
