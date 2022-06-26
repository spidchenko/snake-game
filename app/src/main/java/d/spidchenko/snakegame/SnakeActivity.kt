package d.spidchenko.snakegame

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout

class SnakeActivity : Activity() {

    private lateinit var snakeGame: SnakeGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        snakeGame = SnakeGame(this)
        val gameLayout: LinearLayout = findViewById(R.id.gameLayout)
        gameLayout.addView(snakeGame)
    }

    override fun onResume() {
        super.onResume()
        snakeGame.resume()
    }

    override fun onPause() {
        super.onPause()
        snakeGame.pause()
    }
}