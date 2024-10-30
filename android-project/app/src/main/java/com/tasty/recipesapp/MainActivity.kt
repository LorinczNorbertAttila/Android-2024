package com.tasty.recipesapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var receiverMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        receiverMsg = findViewById(R.id.received_value_id)
//        val intent = intent
//        val str = intent.getStringExtra("message_key")
//        receiverMsg.text = str

        Log.d(TAG, "onCreate: MainActivity created.")
    }
}