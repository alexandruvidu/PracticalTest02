package ro.pub.cs.systems.eim.practicaltest02v2.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ro.pub.cs.systems.eim.practicaltest02v2.R
import java.net.Socket

class PracticalTest02v2ServerActivity : AppCompatActivity() {

    private var isRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test02v2_server)

        val textView = findViewById<TextView>(R.id.textView)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Replace with your laptop's local IP address
                val serverIP = "10.41.39.215"
                val serverPort = 5000

                // Establish a socket connection
                val socket = Socket(serverIP, serverPort)
                val inputStream = socket.inputStream

                while (isActive && isRunning) {
                    // Read data from the server
                    val buffer = ByteArray(1024)
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead == -1) break // Connection closed

                    val serverTime = String(buffer, 0, bytesRead)

                    // Update the UI with the received time
                    withContext(Dispatchers.Main) {
                        textView.text = "Server Time: $serverTime"
                    }
                }

                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    textView.text = "Error: ${e.message}"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the connection when the activity is destroyed
        isRunning = false
    }
}
