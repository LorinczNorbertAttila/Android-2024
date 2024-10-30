package com.tasty.recipesapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var sendText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        Log.d(TAG, "onCreate: SplashActivity created.")

//        sendText = findViewById(R.id.send_text_id)
//        val button: Button = findViewById(R.id.button)
//        button.setOnClickListener {
//            val str = sendText.text.toString()
//            val intent = Intent(this@SplashActivity, MainActivity::class.java)
//            intent.putExtra("message_key", str)
//            startActivity(intent)
//        }
        // Use a HandlerThread to create a background thread
        val handlerThread = HandlerThread("SplashHandlerThread", -10)
        handlerThread.start() // Create a Handler on the new HandlerThread
        val handler = Handler(handlerThread.looper)
        val splashTimeout  = 2500
        handler.postDelayed({
        // Navigate to MainActivity after the delay
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish() },
            splashTimeout.toLong()
        )
    }
}