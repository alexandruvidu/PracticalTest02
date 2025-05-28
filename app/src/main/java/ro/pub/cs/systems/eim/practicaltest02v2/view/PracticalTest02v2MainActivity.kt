package ro.pub.cs.systems.eim.practicaltest02v2.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ro.pub.cs.systems.eim.practicaltest02v2.R
import ro.pub.cs.systems.eim.practicaltest02v2.network.DefinitionAsyncTask

class PracticalTest02v2MainActivity : AppCompatActivity() {

    private lateinit var etWord: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnFetch: Button

    private val getDefClickListener = GetDefClickListener()

    private val definitionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (DefinitionAsyncTask.ACTION_SEND_DEFINITION == intent.action) {
                val definition = intent.getStringExtra(DefinitionAsyncTask.EXTRA_DEFINITION)
                Log.d("MainActivity", "Received definition: $definition")
                tvResult.text = definition ?: "No definition received"
            }
        }
    }

    private inner class GetDefClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val word = etWord.text.toString()
            val definitionAsyncTask = DefinitionAsyncTask(this@PracticalTest02v2MainActivity, object : DefinitionAsyncTask.DefinitionCallback {
                override fun onDefinitionReceived(definition: String) {
                    tvResult.text = definition
                }
            })
            definitionAsyncTask.execute(word)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test02v2_main)

        etWord = findViewById(R.id.etWord)
        btnFetch = findViewById(R.id.btnFetch)
        tvResult = findViewById(R.id.tvResult)

        btnFetch.setOnClickListener(getDefClickListener)

        val button = findViewById<Button>(R.id.connServer)

        // Set a click listener to open the second activity
        button.setOnClickListener {
            // Use an Intent to navigate to the second activity
            val intent = Intent(this@PracticalTest02v2MainActivity, PracticalTest02v2ServerActivity::class.java)
            startActivity(intent)
        }

        // Register the BroadcastReceiver with proper flags for Android 13+
        val filter = IntentFilter(DefinitionAsyncTask.ACTION_SEND_DEFINITION)
        registerReceiver(definitionReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onDestroy() {
        // Unregister the BroadcastReceiver
        unregisterReceiver(definitionReceiver)
        super.onDestroy()
    }
}